package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.MonitoringPoint;
import org.apache.ibatis.annotations.*;
import org.example.model.WeatherData;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.TobaccoLeafDataEnum;
import org.example.model.enums.WeatherType;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface MonitoringPointMapper
{
    /**
     * 根据监测点ID判断该数据是否存在。
     * @param monitoringPointID 监测点ID
     * @return 存在返回 true，否则返回 false
     */
    boolean existsMonitoringPoint(@Param("monitoringPointID") int monitoringPointID);

    /**
     * 插入一条新的监测点数据。
     * @param monitoringPoint 要插入的监测点对象
     * @return 影响的行数
     */
    int insertMonitoringPoint(MonitoringPoint monitoringPoint);

    /**
     * 根据监测点ID逻辑删除监测点（设置 IsDelete = true）。
     * @param monitoringPointID 监测点ID
     * @return 影响的行数
     */
    int deleteMonitoringPointById(@Param("monitoringPointID") int monitoringPointID);

    /**
     * 根据监测点ID更新整条数据。
     * @param monitoringPoint 要更新的监测点对象
     * @return 影响的行数
     */
    int updateMonitoringPointById(MonitoringPoint monitoringPoint);

    /**
     * 恢复被删除的监测点（设置 IsDelete = false）。
     * @param monitoringPointID 监测点ID
     * @return 影响的行数
     */
    int restoreMonitoringPointById(@Param("monitoringPointID") int monitoringPointID);

    /**
     * 获取所有未删除的监测点数据。
     * @return 监测点列表
     */
    List<MonitoringPoint> getAllMonitoringPoints();

    /**
     * 查询所有监测点（包括被逻辑删除的）—— 管理员使用。
     * @return 所有监测点列表
     */
    List<MonitoringPoint> getAllMonitoringPointsWithDeleted();

    /**
     * 查询指定ID的监测点是否已被删除
     * @param monitoringPointID 监测点ID
     * @return 是否已删除（true/false）
     */
    boolean isMonitoringPointDeleted(@Param("monitoringPointID") int monitoringPointID);

    List<MonitoringPoint> getMonitoringPointsBySoilCondition( @Param("soilAttribute") SoilAttribute soilAttribute,
                                                              @Param("minValue") Double minValue,
                                                              @Param("maxValue") Double maxValue);

    /**
     * 根据天气条件查询监测点
     * @param weatherType 天气类型
     * @param minValue 值下限
     * @param maxValue 值上限
     * @return 监测点列表
     */
    List<MonitoringPoint> getMonitoringPointsByWeatherCondition(
            @Param("weatherType") WeatherType weatherType,
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue
    );

    List<MonitoringPoint> getMonitoringPointsByAdministrativeDivision(
            @Param("countyDistrict") String countyDistrict,
            @Param("town") String town,
            @Param("village") String village);

    List<MonitoringPoint>  getMonitoringPointsByTobaccoCondition(
            @Param("tobaccoLeafData") TobaccoLeafDataEnum tobaccoLeafData,
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue
    );

    /**
     * 根据种植面积查询监测点
     * @param minValue 最小种植面积
     * @param maxValue 最大种植面积
     * @return 监测点列表
     */
    List<MonitoringPoint> getMonitoringPointsByPlantingArea(
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue
    );

    /**
     * 根据计划合同量查询监测点
     * @param minValue 最小计划合同量
     * @param maxValue 最大计划合同量
     * @return 监测点列表
     */
    List<MonitoringPoint> getMonitoringPointsByPlannedContractAmount(
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue
    );

    /**
     * 根据收购数量查询监测点
     * @param minValue 最小收购数量
     * @param maxValue 最大收购数量
     * @return 监测点列表
     */
    List<MonitoringPoint> getMonitoringPointsByPurchaseQuantity(
            @Param("minValue") Double minValue,
            @Param("maxValue") Double maxValue
    );

    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countActiveByFilter(@Param("countyDistrict") String countyDistrict);

    /** 统计总数（县/乡/村均可空） */
    long countActiveByRegion(@Param("countyDistrict") String countyDistrict,
                             @Param("town") String town,
                             @Param("village") String village);

    /** 分页查询（县/乡/村均可空） */
    List<MonitoringPoint> pageActiveByRegion(@Param("countyDistrict") String countyDistrict,
                                             @Param("town") String town,
                                             @Param("village") String village,
                                             @Param("limit") int limit,
                                             @Param("offset") int offset);
    /** 硬删除 */
    int hardDeleteExpired(@Param("cutoff") LocalDateTime cutoff);

    //图片存放
    // 1) 只查图片数组：如要最稳妥可改为返回 MonitoringPoint 再取 imagePaths
    List<String> selectImagePathsById(@Param("monitoringPointID") int monitoringPointID);

    // 2) 覆盖整个图片数组
    int updateImagePaths(@Param("monitoringPointID") int monitoringPointID,
                         @Param("imagePaths") java.util.List<String> imagePaths);

    // 3) 原子追加
    int appendOneImagePath(@Param("monitoringPointID") int monitoringPointID,
                           @Param("path") String path);

    // 4) 原子删除
    int removeOneImagePath(@Param("monitoringPointID") int monitoringPointID,
                           @Param("path") String path);

}
