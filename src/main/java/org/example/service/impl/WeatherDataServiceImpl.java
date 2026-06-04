package org.example.service.impl;

import org.example.dao.impl.WeatherDataDaoImpl;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.WeatherListDTO.WeatherDTO;
import org.example.dto.WeatherListDTO.WeatherListDTO;
import org.example.mapper.WeatherDataMapper;
import org.example.model.SoilData;
import org.example.model.WeatherData;
import org.example.response.base.*;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.Weather.WeatherListResponse;
import org.example.service.DataStorageService;
import org.example.service.ISoilDataService;
import org.example.service.IWeatherDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WeatherDataServiceImpl implements IWeatherDataService, DataStorageService<WeatherData>
{
    @Autowired
    private WeatherDataDaoImpl weatherDataDao;

    @Autowired
    private MonitoringPointServiceImpl monitoringPointServiceImpl;
    @Autowired
    private WeatherDataMapper weatherDataMapper;

    /**
     * 插入一条新的天气数据。
     * 先检查天气数据是否存在，若不存在则插入数据。
     *
     * @param weatherDTO 天气数据传输对象
     * @return 插入操作的响应结果，包含插入操作的状态信息等
     */
    @Transactional
    @Override
    public InsertResponse insertWeatherData(WeatherDTO weatherDTO) {
        try {
            if(!monitoringPointServiceImpl.existsMonitoringPoint(weatherDTO.getMonitoringPointId())){
                return new InsertResponse(false, "监测点Id不存在，插入失败");
            }
            WeatherData weatherData = convertToEntity(weatherDTO);
            weatherDataDao.insertWeatherData(weatherData);
            return new InsertResponse(true, "天气数据添加成功");
        } catch (Exception e) {
            return new InsertResponse(false, "添加失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有未删除的天气数据。
     *
     * @return 包含未删除的天气数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public WeatherListResponse getAllActiveWeatherData() {
        try {
            List<WeatherData> weatherDataList = weatherDataDao.getAllActiveWeatherData();
            WeatherListDTO listDTO = convertToWeatherListDTO(weatherDataList);
            return WeatherListResponse.ok(listDTO);
        } catch (Exception e) {
            return WeatherListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 获取所有天气数据。
     *
     * @return 包含所有天气数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public WeatherListResponse getAllWeatherData() {
        try {
            List<WeatherData> weatherDataList = weatherDataDao.getAllWeatherData();
            WeatherListDTO listDTO = convertToWeatherListDTO(weatherDataList);
            return WeatherListResponse.ok(listDTO);
        } catch (Exception e) {
            return WeatherListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据 weatherId 删除天气数据（软删除）。
     * 先检查天气数据是否存在，若存在则进行删除操作。
     *
     * @param weatherId 天气数据的 ID
     * @return 删除操作的响应结果，包含删除操作的状态信息等
     */
    @Transactional
    @Override
    public DeleteResponse deleteWeatherDataByWeatherId(int weatherId) {
        try {
            // 检查天气数据是否存在
            if (!weatherDataDao.checkWeatherIdExists(weatherId)) {
                return new DeleteResponse(false, "天气数据不存在，删除失败");
            }
            weatherDataDao.deleteWeatherDataByWeatherId(weatherId);
            return new DeleteResponse(true, "删除成功");
        } catch (Exception e) {
            return new DeleteResponse(false, "删除失败: " + e.getMessage());
        }
    }

    /**
     * 根据 weatherId 恢复已删除的天气数据。
     * 先检查天气数据是否存在且已删除，若满足条件则进行恢复操作。
     *
     * @param weatherId 天气数据的 ID
     * @return 恢复操作的响应结果，包含恢复操作的状态信息等
     */
    @Transactional
    @Override
    public UpdateResponse restoreWeatherDataByWeatherId(int weatherId) {
        try {
            // 检查天气数据是否存在
            if (!weatherDataDao.checkWeatherIdExists(weatherId)) {
                return new UpdateResponse(false, "天气数据不存在，恢复失败");
            }
            // 检查天气数据是否已删除
            if (!weatherDataDao.isWeatherDataDeleted(weatherId)) {
                return new UpdateResponse(false, "天气数据未删除，无需恢复");
            }
            weatherDataDao.restoreWeatherDataByWeatherId(weatherId);
            return new UpdateResponse(true, "恢复成功");
        } catch (Exception e) {
            return new UpdateResponse(false, "恢复失败: " + e.getMessage());
        }
    }

    /**
     * 检查 weatherId 是否存在，该方法用于内部检验，不返回报文给客户端。
     *
     * @param weatherId 天气数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    @Transactional(readOnly = true)
    @Override
    public boolean checkWeatherIdExists(int weatherId) {
        try {
            return weatherDataDao.checkWeatherIdExists(weatherId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 检查 weatherId 下的数据是否已删除，该方法用于内部检验，不返回报文给客户端。
     *
     * @param weatherId 天气数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    @Transactional(readOnly = true)
    @Override
    public boolean isWeatherDataDeleted(int weatherId) {
        try {
            return weatherDataDao.isWeatherDataDeleted(weatherId);
        } catch (Exception e) {
            // 可以根据实际情况记录日志
            return false;
        }
    }

    /**
     * 根据 monitoringPointId 获取所有天气数据。
     *
     * @param monitoringPointId 监测点 ID
     * @return 包含对应监测点的所有天气数据的响应结果，包含数据列表及相关状态信息
     */
    @Transactional(readOnly = true)
    @Override
    public WeatherListResponse getWeatherDataByMonitoringPointId(int monitoringPointId) {
        try {
            List<WeatherData> weatherDataList = weatherDataDao.getWeatherDataByMonitoringPointId(monitoringPointId);
            WeatherListDTO listDTO = convertToWeatherListDTO(weatherDataList);
            return WeatherListResponse.ok(listDTO);
        } catch (Exception e) {
            return WeatherListResponse.error("查询失败: " + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========
    /**
     * 将 WeatherDTO 转换为 WeatherData 实体对象。
     *
     * @param dto 天气数据传输对象
     * @return 天气数据实体对象
     */
    private WeatherData convertToEntity(WeatherDTO dto) {
        WeatherData weatherData = new WeatherData();
        weatherData.setWeatherId(dto.getWeatherId());
        weatherData.setMonitoringPointId(dto.getMonitoringPointId());
        weatherData.setWeatherType(dto.getWeatherType());
        weatherData.setValue(dto.getValue());
        weatherData.setDescribe(dto.getDescribe());
        weatherData.setAcquisitionCycle(dto.getAcquisitionCycle());
        weatherData.setDataSource(dto.getDataSource());
        weatherData.setSamplingDate(dto.getSamplingDate());
        weatherData.setDelete(dto.isDeleted());

        weatherData.setSamplingTime(dto.getSoilSamplingTime());

        return weatherData;
    }

    /**
     * 将 WeatherData 实体对象转换为 WeatherDTO 传输对象。
     *
     * @param weatherData 天气数据实体对象
     * @return 天气数据传输对象
     */
    private WeatherDTO convertToDTO(WeatherData weatherData) {
        WeatherDTO dto = new WeatherDTO();
        dto.setWeatherId(weatherData.getWeatherId());
        dto.setMonitoringPointId(weatherData.getMonitoringPointId());
        dto.setWeatherType(weatherData.getWeatherType());
        dto.setValue(weatherData.getValue());
        dto.setDescribe(weatherData.getDescribe());
        dto.setAcquisitionCycle(weatherData.getAcquisitionCycle());
        dto.setDataSource(weatherData.getDataSource());
        dto.setSamplingDate(weatherData.getSamplingDate());
        dto.setDeleted(weatherData.isDelete());

        dto.setSoilSamplingTime(weatherData.getSamplingTime());

        return dto;
    }

    /**
     * 将 WeatherData 列表转换为 WeatherListDTO 传输对象。
     *
     * @param weatherDataList 天气数据实体列表
     * @return 包含天气数据传输对象列表的 WeatherListDTO
     */
    private WeatherListDTO convertToWeatherListDTO(List<WeatherData> weatherDataList) {
        List<WeatherDTO> dtoList = weatherDataList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new WeatherListDTO(dtoList);
    }


    /**
     * 根据 WeatherId 更新天气数据。
     * 执行更新操作前，不进行额外的特殊检查（假设传入的 WeatherData 对象数据合理），直接尝试更新。
     *
     * @param weatherData 包含要更新的天气数据的对象，其中应包含 WeatherId 以及其他要更新的字段值
     * @return 更新操作的响应结果，包含更新操作的状态信息等
     */
    @Transactional
    @Override
    public UpdateResponse updateWeatherDataByWeatherId(WeatherDTO weatherData) {
        try {
            int weatherId = weatherData.getWeatherId();
            if(!monitoringPointServiceImpl.existsMonitoringPoint(weatherData.getMonitoringPointId())){
                return new UpdateResponse(false, "监测点Id不存在，更新失败");
            }

            // 检查 weatherId 是否存在
            if (!checkWeatherIdExists(weatherId)) {
                return new UpdateResponse(false, "要更新的天气数据的 WeatherId 不存在，更新失败");
            }
            // 调用数据访问层方法执行更新操作，并获取受影响的行数
            int rowsAffected = weatherDataDao.updateWeatherDataByWeatherId(convertToEntity(weatherData));
            if (rowsAffected > 0) {
                return new UpdateResponse(true, "根据 WeatherId 更新天气数据成功");
            } else {
                return new UpdateResponse(false, "根据 WeatherId 更新天气数据失败");
            }
        } catch (Exception e) {
            return new UpdateResponse(false, "更新失败: " + e.getMessage());
        }
    }

    /**
     * 实现 DataStorageService 接口的 saveAll 方法
     * @param dataList 要保存的天气数据列表
     */
    @Transactional
    @Override
    public void saveAll(List<WeatherData> dataList) {
        for (WeatherData data : dataList) {
            try {
                if(!monitoringPointServiceImpl.existsMonitoringPoint(data.getMonitoringPointId())){
                    continue; // 跳过监测点ID不存在的数据
                }
                weatherDataDao.insertWeatherData(data);
            } catch (Exception e) {
                // 可以根据实际情况记录日志
            }
        }
    }




    // 分页查询
    @Override
    public PageResult<WeatherDTO> pageByCounty(PageRequest pr, String countyDistrict) {
        return PagingHelper.page(
                pr,
                // 1) 总数
                () -> weatherDataMapper.countByCounty(countyDistrict),
                // 2) 当前页数据
                () -> weatherDataMapper.findByCountyAndPage(
                        countyDistrict, pr.getSize(), pr.offset()),
                // 3) 行 -> DTO
                this::convertToDTO   //
        );
    }

    /** 按县区获取某字段全部取值（字段名白名单） */
    @Override
    public List<Object> listFieldValuesByCounty(String countyDistrict,  WeatherDataField field) {
        Objects.requireNonNull(field, "field 不能为空");
        // 传入 Mapper 的 field 是字符串，但来自这个枚举，安全白名单
        return weatherDataMapper.listFieldValuesByCounty(countyDistrict, field.name());
    }

    /** 调试输出，可在 PrintForms 或控制台调用 */
    public void debugPrintByCounty(int page, int size, String county) {
        if (page < 1) page = 1;
        if (size < 1) size = 10;
        int offset = (page - 1) * size;

        long total = weatherDataMapper.countByCounty( county);
        long totalPages = (total + size - 1) / size;

        List<WeatherData> rows =
                weatherDataMapper.findByCountyAndPage( county, size, offset);

        System.out.printf("分页结果：page=%d/%d, size=%d, total=%d%n",
                page, totalPages, size, total);
        rows.forEach(System.out::println); // 记得 DTO 已经重写 toString()
    }

    @Override
    public List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, WeatherDataField field) {
        // 直接用白名单字段枚举的 name() 传给 Mapper 的 <choose/>
        return weatherDataMapper.listFieldValuesByRegion(countyDistrict, town, village, field.name());
    }




}
