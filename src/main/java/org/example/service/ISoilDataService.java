package org.example.service;

import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.model.SoilData;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SoilData.SoilDataListResponse;

import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;



import java.util.List;

/**
 * SoilDataService 接口
 * 提供操作土壤数据的业务方法
 */
public interface ISoilDataService
{

    /**
     * 查询所有未删除的土壤数据
     *
     * @return 包含所有未删除土壤数据的 SoilDataListResponse 对象
     */
    SoilDataListResponse findAll();

    /**
     * 查询所有土壤数据（包括已删除的），仅管理员使用
     *
     * @return 包含所有土壤数据的 SoilDataListResponse 对象
     */
    SoilDataListResponse findAllForAdmin();

    /**
     * 插入一条土壤数据，并进行分类处理
     *
     * @param soilDataDTO 待插入的土壤数据传输对象
     * @return 插入操作的 InsertResponse 响应结果
     */
    InsertResponse insertWithClassification(SoilDataDTO soilDataDTO);

    /**
     * 根据土壤数据 ID 进行软删除操作
     *
     * @param soilDataID 待删除的土壤数据 ID
     * @return 删除操作的 DeleteResponse 响应结果
     */
    DeleteResponse softDeleteById(int soilDataID);

    /**
     * 根据土壤数据 ID 恢复已删除的土壤数据
     *
     * @param soilDataID 待恢复的土壤数据 ID
     * @return 恢复操作的 UpdateResponse 响应结果
     */
    UpdateResponse restoreById(int soilDataID);

    /**
     * 更新土壤数据，并进行分类处理
     *
     * @param soilDataDTO 包含更新后土壤数据的传输对象
     * @return 更新操作的 UpdateResponse 响应结果
     */
    UpdateResponse updateDataWithClassification(SoilDataDTO soilDataDTO);

    /**
     * 根据特定的土壤属性更新分类
     *
     * @param soilAttribute 特定的土壤属性
     * @return 更新操作的 UpdateResponse 响应结果
     */
    UpdateResponse updateClassificationForSpecificAttribute(String soilAttribute);

    /**
     * 检查指定的土壤数据 ID 是否存在
     *
     * @param soilDataID 土壤数据 ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean existsById(Long soilDataID);

    /**
     * 检查指定的土壤数据 ID 是否已被删除
     *
     * @param soilDataID 土壤数据 ID
     * @return 如果已被删除返回 true，否则返回 false
     */
    boolean isDeletedById(Long soilDataID);

    SoilDataListResponse findById(int soilDataID);



    /**
     * 按县区分页查询
     * @param pr
     * @param countyDistrict
     * @return
     */
    PageResult<SoilDataDTO> pageByCounty(PageRequest pr, String countyDistrict);

    /** 按县区获取某字段全部取值（字段名白名单） */
    List<Object> listFieldValuesByCounty(String countyDistrict, SoilDataField field);

    enum SoilDataField {
        Value, SamplingDate, Classification, SoilAttribute
    }
    public void debugPrintByCounty(int page, int size, String county);

    List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village, SoilDataField field);


    //新增数据插入方法
    InsertResponse batchInsertWithClassification(List<SoilDataDTO> soilDataDTOList);

}
