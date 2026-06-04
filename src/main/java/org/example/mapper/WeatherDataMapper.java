package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SoilData;
import org.example.model.WeatherData;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface WeatherDataMapper
{
    /**
     * 插入一条新的天气数据。
     *
     * @param weatherData 天气数据对象
     */
    int insertWeatherData(WeatherData weatherData);

    /**
     * 获取所有未删除的天气数据。
     *
     * @return 未删除的天气数据列表
     */
    List<WeatherData> getAllActiveWeatherData();

    /**
     * 获取所有天气数据。
     *
     * @return 所有天气数据列表
     */
    List<WeatherData> getAllWeatherData();

    /**
     * 根据 weatherId 删除天气数据（软删除）。
     *
     * @param weatherId 天气数据的 ID
     */
    int deleteWeatherDataByWeatherId(@Param("weatherId") int weatherId);

    /**
     * 根据 weatherId 恢复已删除的天气数据。
     *
     * @param weatherId 天气数据的 ID
     */
    int restoreWeatherDataByWeatherId(@Param("weatherId") int weatherId);

    /**
     * 查询 weatherId 是否存在。
     *
     * @param weatherId 天气数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean checkWeatherIdExists(@Param("weatherId") int weatherId);

    /**
     * 查询 weatherId 下的数据是否已删除。
     *
     * @param weatherId 天气数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    boolean isWeatherDataDeleted(@Param("weatherId") int weatherId);

    /**
     * 根据 monitoringPointId 获取所有天气数据。
     *
     * @param monitoringPointId 监测点 ID
     * @return 对应监测点的所有天气数据列表
     */
    List<WeatherData> getWeatherDataByMonitoringPointId(@Param("monitoringPointId") int monitoringPointId);

    int updateWeatherDataByWeatherId(WeatherData weatherData);

    /**
     * 硬删除
     */
    int hardDeleteExpired(@Param("cutoff") LocalDateTime cutoff);


    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countByCounty(@Param("countyDistrict") String countyDistrict);
    /** 分页查询未删除的数据（可选县区过滤） */
    List<WeatherData> findByCountyAndPage(@Param("countyDistrict") String countyDistrict,
                                       @Param("limit") int limit,
                                       @Param("offset") int offset);

    List<Object> listFieldValuesByCounty(@Param("countyDistrict") String countyDistrict,
                                         @Param("field") String field);
    //测试用代码
    public void debugPrintByCounty(String countyDistrict);

    //通过县/乡/村来查询队友土壤数据
    List<Object> listFieldValuesByRegion(@Param("countyDistrict") String countyDistrict,
                                         @Param("town") String town,
                                         @Param("village") String village,
                                         @Param("field") String field);

}
