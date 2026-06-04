package org.example.service;
import org.example.dto.SmokingEvaluationDataListDTO.SmokingEvaluationDataDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.model.SmokingEvaluationData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SmokingEvaluationData.SmokingEvaluationDataListResponse;

import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.CommonResponse;

import java.util.List;

public interface ISmokingEvaluationDataService
{
    /**
     * 插入一条新的吸烟评估数据。
     *
     * @param smokingEvaluationDataDTO 吸烟评估数据传输对象
     * @return 插入操作的响应结果，包含插入操作的状态信息等
     */
    InsertResponse insertSmokingEvaluationData(SmokingEvaluationDataDTO smokingEvaluationDataDTO);

    /**
     * 获取所有未删除的吸烟评估数据。
     *
     * @return 包含未删除的吸烟评估数据的响应结果，包含数据列表及相关状态信息
     */
    SmokingEvaluationDataListResponse getAllActiveSmokingEvaluationData();

    /**
     * 获取所有吸烟评估数据。
     *
     * @return 包含所有吸烟评估数据的响应结果，包含数据列表及相关状态信息
     */
    SmokingEvaluationDataListResponse getAllSmokingEvaluationData();

    /**
     * 根据smokingEvaluationId删除吸烟评估数据（软删除）。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 删除操作的响应结果，包含删除操作的状态信息等
     */
    DeleteResponse deleteSmokingEvaluationDataById(int smokingEvaluationId);

    /**
     * 根据smokingEvaluationId恢复已删除的吸烟评估数据。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 恢复操作的响应结果，包含恢复操作的状态信息等
     */
    UpdateResponse restoreSmokingEvaluationDataById(int smokingEvaluationId);

    /**
     * 查询smokingEvaluationId是否存在。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return 如果存在返回true，否则返回false
     */
    boolean checkSmokingEvaluationIdExists(int smokingEvaluationId);

    /**
     * 查询smokingEvaluationId下的数据是否已删除。
     *
     * @param smokingEvaluationId 吸烟评估数据的ID
     * @return如果已删除返回true，否则返回false
     */
    boolean isSmokingEvaluationDataDeleted(int smokingEvaluationId);

    /**
     * 根据monitoringPointID获取所有吸烟评估数据。
     *
     * @param monitoringPointId 监测点ID
     * @return 包含对应监测点的所有吸烟评估数据的响应结果，包含数据列表及相关状态信息
     */
    SmokingEvaluationDataListResponse getSmokingEvaluationDataByMonitoringPointId(int monitoringPointId);

    UpdateResponse updateSmokingEvaluationDataById(SmokingEvaluationDataDTO smokingEvaluationData);




    /**
     * 按县区分页查询
     * @param pr
     * @param countyDistrict
     * @return
     */
    PageResult<SmokingEvaluationDataDTO> pageByCounty(PageRequest pr, String countyDistrict);

    /** 按县区获取某字段全部取值（字段名白名单） */
    List<Object> listFieldValuesByCounty(String countyDistrict,ISmokingEvaluationDatafield field);

    enum ISmokingEvaluationDatafield {
        SmokingEvaluationData, SamplingDate, Describe
    }
    public void debugPrintByCounty(int page, int size, String county);

    List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, ISmokingEvaluationDatafield field);

}
