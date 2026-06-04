package org.example.service;

import org.example.dto.MonitoringPointListDTO.MonitoringPointDTO;
import org.example.model.MonitoringPoint;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.TobaccoLeafDataEnum;
import org.example.model.enums.WeatherType;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.MonitoringPoint.MonitoringPointListResponse;


import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.dto.MonitoringPointListDTO.MonitoringPointDTO;

import java.util.List;


/**
 * 监测点业务逻辑层接口。
 */
public interface IMonitoringPointService
{
    /**
     * 检查某个监测点 ID 是否存在。
     *
     * @param monitoringPointID 待检查的监测点 ID
     * @return 如果监测点 ID 存在则返回 true，否则返回 false
     */
    boolean existsMonitoringPoint(int monitoringPointID);

    /**
     * 插入一个新的监测点记录。
     *
     * @param monitoringPointDTO 包含新监测点信息的数据传输对象
     * @return 插入操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    InsertResponse insertMonitoringPoint(MonitoringPointDTO monitoringPointDTO);

    /**
     * 根据 ID 逻辑删除一个监测点。
     *
     * @param monitoringPointID 待删除的监测点 ID
     * @return 删除操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    DeleteResponse deleteMonitoringPointById(int monitoringPointID);

    /**
     * 根据 ID 修改整个监测点对象。
     *
     * @param monitoringPointDTO 包含更新后监测点信息的数据传输对象
     * @return 更新操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    UpdateResponse updateMonitoringPointById(MonitoringPointDTO monitoringPointDTO);

    /**
     * 恢复某个被逻辑删除的监测点。
     *
     * @param monitoringPointID 待恢复的监测点 ID
     * @return 恢复操作的响应结果，包含操作是否成功的标志和相应的消息
     */
    UpdateResponse restoreMonitoringPointById(int monitoringPointID);

    /**
     * 查询所有未被逻辑删除的监测点。
     *
     * @return 包含未删除监测点列表的响应结果，包含操作是否成功的标志、消息和监测点列表数据
     */
    MonitoringPointListResponse getAllMonitoringPoints();

    /**
     * 查询所有监测点（包括被逻辑删除的）—— 仅管理员使用。
     *
     * @return 包含所有监测点列表的响应结果，包含操作是否成功的标志、消息和监测点列表数据
     */
    MonitoringPointListResponse getAllMonitoringPointsWithDeleted();

    /**
     * 查询指定 ID 的监测点是否已删除。
     *
     * @param monitoringPointID 待查询的监测点 ID
     * @return 如果监测点已被删除则返回 true，否则返回 false
     */
    boolean isMonitoringPointDeleted(int monitoringPointID);

    void printAllMonitoringPoints();

    /**
     * 根据土壤属性和值范围查询监测点
     * @param soilAttribute 土壤属性（枚举类型）
     * @param minValue 值范围最小值
     * @param maxValue 值范围最大值
     * @return 符合条件的监测点列表响应
     */
    MonitoringPointListResponse getMonitoringPointsBySoilCondition(
            SoilAttribute soilAttribute,
            Double minValue,
            Double maxValue
    );

    /**
     * 根据天气条件查询监测点
     * @param weatherType 天气类型（枚举）
     * @param minValue 值下限
     * @param maxValue 值上限
     * @return 统一响应结果，包含监测点列表
     */
    MonitoringPointListResponse getMonitoringPointsByWeatherCondition(
            WeatherType weatherType,
            Double minValue,
            Double maxValue
    );

    /**
     * 根据行政区划查询监测点（支持县/乡/村模糊查询）
     * @param countyDistrict 县区（可选）
     * @param town 镇/乡（可选）
     * @param village 村（可选）
     * @return 监测点列表响应
     */
    MonitoringPointListResponse getMonitoringPointsByAdministrativeDivision(
            String countyDistrict,
            String town,
            String village
    );

    /**
     * 根据烟叶数据条件查询监测点
     * @param tobaccoLeafData 烟叶数据类型（枚举）
     * @param minValue 值下限
     * @param maxValue 值上限
     * @return 监测点列表响应
     */
    MonitoringPointListResponse getMonitoringPointsByTobaccoCondition(
            TobaccoLeafDataEnum tobaccoLeafData,
            Double minValue,
            Double maxValue
    );

    /**
     * 根据种植面积查询监测点
     * @param minValue 最小种植面积（亩）
     * @param maxValue 最大种植面积（亩）
     * @return 监测点列表响应
     */
    MonitoringPointListResponse getMonitoringPointsByPlantingArea(
            Double minValue,
            Double maxValue
    );

    /**
     * 根据计划合同量查询监测点
     * @param minValue 最小计划合同量（公斤）
     * @param maxValue 最大计划合同量（公斤）
     * @return 监测点列表响应
     */
    MonitoringPointListResponse getMonitoringPointsByPlannedContractAmount(
            Double minValue,
            Double maxValue
    );

    /**
     * 根据收购数量查询监测点
     * @param minValue 最小收购数量（公斤）
     * @param maxValue 最大收购数量（公斤）
     * @return 监测点列表响应
     */
    MonitoringPointListResponse getMonitoringPointsByPurchaseQuantity(
            Double minValue,
            Double maxValue
    );

    /**
     * 简单分页返回包（不依赖你项目里的 PageResult，避免字段名不一致）
     */
    class RegionPage<T> {
        public final int page;   // 1-based
        public final int size;
        public final long total;
        public final List<T> list;
        public RegionPage(int page, int size, long total, List<T> list) {
            this.page = page;
            this.size = size;
            this.total = total;
            this.list = list;
        }
    }

    /**
     * 分页查询监测点（未删除），县/乡/村均可为空
     * @param countyDistrict 县区，可空
     * @param town 乡镇，可空
     * @param village 村，可空
     * @param page 第几页（1开始）
     * @param size 每页条数
     */
    RegionPage<MonitoringPoint> pageActiveByRegion(String countyDistrict, String town, String village,
                                                   int page, int size);



}
