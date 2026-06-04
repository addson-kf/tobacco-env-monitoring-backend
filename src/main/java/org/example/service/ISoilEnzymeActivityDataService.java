package org.example.service;
import org.apache.ibatis.annotations.Param;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.SoilEnzymeActivityDataListDTO.SoilEnzymeActivityDataDTO;
import org.example.dto.SoilEnzymeActivityDataListDTO.SoilEnzymeActivityDataListDTO;
import org.example.model.SoilEnzymeActivityData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SoilEnzymeActivity.SoilEnzymeActivityDataListResponse;

import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.CommonResponse;

import java.util.List;

/**
 * 土壤酶活性数据服务接口
 * 定义了与土壤酶活性数据相关的业务操作
 */
public interface ISoilEnzymeActivityDataService
{
    /**
     * 插入土壤酶活性数据
     *
     * @param soilEnzymeActivityData 土壤酶活性数据实体
     * @return 插入操作的响应结果
     */
    InsertResponse insertSoilEnzymeActivityData(SoilEnzymeActivityDataDTO soilEnzymeActivityData);

    /**
     * 获取所有非删除的土壤酶活性数据
     *
     * @return 包含所有非删除土壤酶活性数据的响应结果
     */
    SoilEnzymeActivityDataListResponse getAllActiveSoilEnzymeActivityData();

    /**
     * 获取所有土壤酶活性数据
     *
     * @return 包含所有土壤酶活性数据的响应结果
     */
    SoilEnzymeActivityDataListResponse getAllSoilEnzymeActivityData();

    /**
     * 根据 SoilEnzymeActivityId 删除数据
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 删除操作的响应结果
     */
    DeleteResponse deleteSoilEnzymeActivityDataById(int soilEnzymeActivityId);

    /**
     * 根据 SoilEnzymeActivityId 恢复数据
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 恢复操作的响应结果
     */
    UpdateResponse restoreSoilEnzymeActivityDataById(int soilEnzymeActivityId);

    /**
     * 查询 SoilEnzymeActivityId 是否存在
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 是否存在该 ID 的布尔值
     */
    boolean checkSoilEnzymeActivityIdExists(int soilEnzymeActivityId);

    /**
     * 查询 SoilEnzymeActivityId 下的数据是否已删除
     *
     * @param soilEnzymeActivityId 土壤酶活性数据的 ID
     * @return 数据是否已删除的布尔值
     */
    boolean isSoilEnzymeActivityDataDeleted(int soilEnzymeActivityId);

    /**
     * 根据 MonitoringPointId 查询土壤酶活性数据
     *
     * @param monitoringPointId 监测点 ID
     * @return 包含指定监测点下土壤酶活性数据的响应结果
     */
    SoilEnzymeActivityDataListResponse getSoilEnzymeActivityDataByMonitoringPointId(int monitoringPointId);

    /**
     * 根据 SoilEnzymeActivityId 更新土壤酶活性数据
     *
     * @param soilEnzymeActivityData 土壤酶活性数据实体
     * @return 更新操作的响应结果
     */
    UpdateResponse updateSoilEnzymeActivityDataById(SoilEnzymeActivityDataDTO soilEnzymeActivityData);



    /**
     * 按县区分页查询
     * @param pr
     * @param countyDistrict
     * @return
     */
    PageResult<SoilEnzymeActivityDataDTO> pageByCounty(PageRequest pr, String countyDistrict);
    /** 按县区获取某字段全部取值（字段名白名单） */
    List<Object> listFieldValuesByCounty(String countyDistrict, ISoilEnzymeActivityDataService.ISoilEnzymeActivityDataField field);

    enum ISoilEnzymeActivityDataField {
        Value, SamplingDate,Describe
    }
    //测试用代码
    public void debugPrintByCounty(int page, int size, String county);
//县区乡村查询
    List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, ISoilEnzymeActivityDataField field);

}
