package org.example.controller;
import org.example.config.ApiConfig;
import org.example.dto.WeatherListDTO.WeatherDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Weather.WeatherListResponse;
import org.example.service.ISoilDataService;
import org.example.service.IWeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 天气数据控制器，处理天气数据相关请求。
 */
/**
 * 天气数据控制器，处理天气数据相关请求。
 */
@RestController
@RequestMapping("${api.weather.base-path}")
@CrossOrigin(origins = "*")// 暂时允许所有域名访问
public class WeatherDataController {

    @Autowired
    private IWeatherDataService weatherDataService;

    @Autowired
    private ApiConfig apiConfig;

    /**
     * 插入新天气数据
     */
    @PostMapping("${api.weather.endpoints.insert}")
    public InsertResponse insertWeatherData(@RequestBody WeatherDTO weatherDTO) {
        return weatherDataService.insertWeatherData(weatherDTO);
    }

    /**
     * 获取未删除的天气数据
     */
    @GetMapping("${api.weather.endpoints.active}")
    public WeatherListResponse getAllActiveWeatherData() {
        return weatherDataService.getAllActiveWeatherData();
    }

    /**
     * 获取所有天气数据（含已删除）
     */
    @GetMapping("${api.weather.endpoints.all}")
    public WeatherListResponse getAllWeatherData() {
        return weatherDataService.getAllWeatherData();
    }

    /**
     * 软删除天气数据
     */
    @DeleteMapping("${api.weather.endpoints.delete}")
    public DeleteResponse deleteWeatherDataByWeatherId(@PathVariable int weatherId) {
        return weatherDataService.deleteWeatherDataByWeatherId(weatherId);
    }

    /**
     * 恢复已删除的天气数据
     */
    @PostMapping("${api.weather.endpoints.restore}")
    public UpdateResponse restoreWeatherDataByWeatherId(@PathVariable int weatherId) {
        return weatherDataService.restoreWeatherDataByWeatherId(weatherId);
    }

    /**
     * 根据监测点ID获取天气数据
     */
    @GetMapping("${api.weather.endpoints.monitoring}")
    public WeatherListResponse getWeatherDataByMonitoringPointId(@PathVariable int monitoringPointId) {
        return weatherDataService.getWeatherDataByMonitoringPointId(monitoringPointId);
    }

    /**
     * 修改天气数据
     */
    @PostMapping("${api.weather.endpoints.update}")
    public UpdateResponse updateWeatherDataByWeatherId(@RequestBody WeatherDTO weatherData) {
        return weatherDataService.updateWeatherDataByWeatherId(weatherData);
    }


    @GetMapping("/field-values/by-region")
    public List<Object> listFieldValuesByRegion(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam IWeatherDataService.WeatherDataField field
    ) {
        return weatherDataService.listFieldValuesByRegion(county, town, village, field);
    }
}
