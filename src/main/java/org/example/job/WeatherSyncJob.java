package org.example.job;
import lombok.RequiredArgsConstructor;
import org.example.dto.MonitoringPointListDTO.MonitoringPointDTO;
import org.example.dto.WeatherListDTO.WeatherDTO;
import org.example.model.enums.WeatherType;
import org.example.response.common.MonitoringPoint.MonitoringPointListResponse;
import org.example.service.IMonitoringPointService;
import org.example.service.IWeatherDataService;
import org.example.service.IWeatherExternalClient;
import org.example.util.GeoUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WeatherSyncJob {

    private final IWeatherExternalClient weatherExternalClient;
    private final IMonitoringPointService monitoringPointService;
    private final IWeatherDataService weatherDataService;

    // ✅ 按你现在的 yml 前缀
    @Value("${api.weather.sync.enabled:true}")
    private boolean enabled;

    // ✅ 按你现在的 yml 前缀
    @Scheduled(fixedRateString = "${api.weather.sync.fixed-rate-ms:1800000}")
    public void syncCurrentWeather() {
        if (!enabled) return;

        List<MonitoringPointDTO> points = loadAllActiveMonitoringPoints();
        if (points.isEmpty()) return;

        for (MonitoringPointDTO mp : points) {
            try {
                double[] coords = mp.getCoordinates();
                if (coords == null || coords.length != 2) {
                    continue;
                }

                // 1) 解析经纬度（兼容 [lat,lon] 或 [lon,lat]）
                double[] latlon = GeoUtil.parseLatLon(coords);

                // 2) 获取外部实时天气
                Map<String, Object> current =
                        weatherExternalClient.fetchCurrent(latlon[0], latlon[1]);

                if (current == null || current.isEmpty()) continue;

                // 3) 写入数据库
                insertWeatherData(mp.getMonitoringPointID(), "temperature_2m", current.get("temperature_2m"));
                insertWeatherData(mp.getMonitoringPointID(), "relative_humidity_2m", current.get("relative_humidity_2m"));
                insertWeatherData(mp.getMonitoringPointID(), "precipitation", current.get("precipitation"));
                insertWeatherData(mp.getMonitoringPointID(), "wind_speed_10m", current.get("wind_speed_10m"));
                insertWeatherData(mp.getMonitoringPointID(), "wind_direction_10m", current.get("wind_direction_10m"));

            } catch (Exception ignored) {
            }
        }
    }

    /**
     * 你当前接口里没有 findAllActive()，
     * 所以这里用 getAllMonitoringPoints() 拿未删除监测点
     */
    private List<MonitoringPointDTO> loadAllActiveMonitoringPoints() {
        try {
            MonitoringPointListResponse resp = monitoringPointService.getAllMonitoringPoints();
            if (resp == null || resp.getData() == null) return Collections.emptyList();

            // 你的 DTO 风格通常是 data.getMonitoringPointList()
            List<MonitoringPointDTO> list = resp.getData().getMonitoringPoints();
            return list != null ? list : Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    private void insertWeatherData(int monitoringPointId, String openMeteoKey, Object valueObj) {
        if (valueObj == null) return;

        Double value;
        try {
            value = Double.valueOf(valueObj.toString());
        } catch (Exception e) {
            return;
        }

        WeatherType type = mapWeatherType(openMeteoKey);
        if (type == null) return;

        WeatherDTO weatherDTO = new WeatherDTO();
        weatherDTO.setMonitoringPointId(monitoringPointId);
        weatherDTO.setWeatherType(type);
        weatherDTO.setValue(value);
        weatherDTO.setDescribe("实时同步");
        weatherDTO.setAcquisitionCycle("sync");
        weatherDTO.setDataSource("open-meteo");
        weatherDTO.setSamplingDate(LocalDateTime.now());
        weatherDTO.setSoilSamplingTime("实时");
        weatherDTO.setDeleted(false);

        weatherDataService.insertWeatherData(weatherDTO);
    }

    /**
     * ✅ 关键：安全映射 Open-Meteo 字段 -> 你的 WeatherType 枚举
     *
     * 你只需要确保你的 WeatherType 中至少存在这些之一：
     * TEMPERATURE_2M / RELATIVE_HUMIDITY_2M / PRECIPITATION / WIND_SPEED_10M / WIND_DIRECTION_10M
     *
     * 如果你的枚举名字不同，就只改这里，不动其它逻辑。
     */
    private WeatherType mapWeatherType(String key) {
        try {
            return switch (key) {
                case "temperature_2m" -> WeatherType.air_temperature;
                case "relative_humidity_2m" -> WeatherType.air_humidity;
                case "precipitation" -> WeatherType.precipitation;
                case "wind_speed_10m" -> WeatherType.wind_speed;
                case "wind_direction_10m" -> WeatherType.wind_direction;
                default -> null;
            };
        } catch (Exception e) {
            return tryNormalizeEnum(key);
        }
    }

    /**
     * 兜底：把 temperature_2m 这类 key 变成 TEMPERATURE_2M 再尝试 valueOf
     */
    private WeatherType tryNormalizeEnum(String key) {
        try {
            String normalized = key.toUpperCase().replace('-', '_');
            return WeatherType.valueOf(normalized);
        } catch (Exception e) {
            return null;
        }
    }
}