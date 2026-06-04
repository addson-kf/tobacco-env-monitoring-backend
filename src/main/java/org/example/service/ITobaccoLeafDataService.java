package org.example.service;

import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.TobaccoLeafDataListDTO.TobaccoLeafDataDTO;
import org.example.model.TobaccoLeafData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.TobaccoLeafData.TobaccoLeafDataListResponse;

import java.util.List;
public interface ITobaccoLeafDataService
{
    /**
     * 插入一条新的烟草叶片数据。
     * 先检查烟草叶片数据是否存在，若不存在则插入数据。
     *
     * @param tobaccoLeafDataDTO 烟草叶片数据传输对象
     * @return 插入操作的响应结果，包含插入操作的状态信息等
     */
    InsertResponse insertTobaccoLeafData(TobaccoLeafDataDTO tobaccoLeafDataDTO);

    /**
     * 获取所有未删除的烟草叶片数据。
     *
     * @return 包含未删除的烟草叶片数据的响应结果，包含数据列表及相关状态信息
     */
    TobaccoLeafDataListResponse getAllActiveTobaccoLeafData();

    /**
     * 获取所有烟草叶片数据。
     *
     * @return 包含所有烟草叶片数据的响应结果，包含数据列表及相关状态信息
     */
    TobaccoLeafDataListResponse getAllTobaccoLeafData();

    /**
     * 根据 TobaccoDataId 删除烟草叶片数据（软删除）。
     * 先检查烟草叶片数据是否存在，若存在则进行删除操作。
     *
     * @param tobaccoDataId 烟草叶片数据的 ID
     * @return 删除操作的响应结果，包含删除操作的状态信息等
     */
    DeleteResponse deleteTobaccoLeafDataByTobaccoDataId(int tobaccoDataId);

    /**
     * 根据 TobaccoDataId 恢复已删除的烟草叶片数据。
     * 先检查烟草叶片数据是否存在且已删除，若满足条件则进行恢复操作。
     *
     * @param tobaccoDataId 烟草叶片数据的 ID
     * @return 恢复操作的响应结果，包含恢复操作的状态信息等
     */
    UpdateResponse restoreTobaccoLeafDataByTobaccoDataId(int tobaccoDataId);

    /**
     * 检查 tobaccoDataId 是否存在，该方法用于内部检验，不返回报文给客户端。
     *
     * @param tobaccoDataId 烟草叶片数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean checkTobaccoDataIdExists(int tobaccoDataId);

    /**
     * 检查 tobaccoDataId 下的数据是否已删除，该方法用于内部检验，不返回报文给客户端。
     *
     * @param tobaccoDataId 烟草叶片数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    boolean isTobaccoLeafDataDeleted(int tobaccoDataId);

    /**
     * 根据 monitoringPointId 获取所有烟草叶片数据。
     *
     * @param monitoringPointId 监测点 ID
     * @return 包含对应监测点的所有烟草叶片数据的响应结果，包含数据列表及相关状态信息
     */
    TobaccoLeafDataListResponse getTobaccoLeafDataByMonitoringPointId(int monitoringPointId);

    /**
     * 根据 TobaccoDataId 更新烟草叶片数据。
     * 执行更新操作前，检查要更新的烟草叶片数据的 TobaccoDataId 是否存在
     *
     * @param tobaccoLeafData 包含要更新的烟草叶片数据的对象，其中应包含 TobaccoDataId 以及其他要更新的字段值
     * @return 更新操作的响应结果，包含更新操作的状态信息等
     */
    UpdateResponse updateTobaccoLeafDataByTobaccoDataId(TobaccoLeafDataDTO tobaccoLeafData);


    /**
     * 按县区分页查询
     * @param pr
     * @param countyDistrict
     * @return
     */
    PageResult<TobaccoLeafDataDTO> pageByCounty(PageRequest pr, String countyDistrict);

    /** 按县区获取某字段全部取值（字段名白名单） */
    List<Object> listFieldValuesByCounty(String countyDistrict,TobaccoLeafDataField field);

    enum TobaccoLeafDataField {
        Value, SamplingDate, Classification, TobaccoLeafData
    }
    public void debugPrintByCounty(int page, int size, String county);
}
