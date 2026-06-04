package org.example.service;

import org.example.dto.SanMingBasicDataListDTO.SanMingBasicDataDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.response.common.SanMingBasicData.SanMingBasicDataListResponse;

import java.util.List;

public interface ISanMingBasicDataService
{
    /**
     * 插入一条新的三明基础数据。
     *
     * @param sanMingBasicDataDTO 三明基础数据传输对象
     * @return 插入操作的响应结果
     */
    InsertResponse insertSanMingBasicData(SanMingBasicDataDTO sanMingBasicDataDTO);

    /**
     * 获取所有未删除的三明基础数据。
     *
     * @return 包含未删除数据的响应结果
     */
    SanMingBasicDataListResponse getAllActiveSanMingBasicData();

    /**
     * 获取所有三明基础数据（包括已删除）。
     *
     * @return 包含所有数据的响应结果
     */
    SanMingBasicDataListResponse getAllSanMingBasicData();

    /**
     * 根据 ID 软删除三明基础数据。
     *
     * @param id 数据 ID
     * @return 删除操作的响应结果
     */
    DeleteResponse deleteSanMingBasicDataById(int id);

    /**
     * 根据 ID 恢复已删除的三明基础数据。
     *
     * @param id 数据 ID
     * @return 恢复操作的响应结果
     */
    UpdateResponse restoreSanMingBasicDataById(int id);

    /**
     * 检查数据 ID 是否存在。
     *
     * @param id 数据 ID
     * @return 存在返回 true，否则 false
     */
    boolean checkIdExists(int id);

    /**
     * 检查数据是否已被软删除。
     *
     * @param id 数据 ID
     * @return 已删除返回 true，否则 false
     */
    boolean isSanMingBasicDataDeleted(int id);


    /**
     * 根据 ID 更新三明基础数据。
     *
     * @param sanMingBasicDataDTO 包含更新信息的 DTO（需包含 ID）
     * @return 更新操作的响应结果
     */
    UpdateResponse updateSanMingBasicDataById(SanMingBasicDataDTO sanMingBasicDataDTO);

    /**
     * 根据监测点 ID 查询未删除的三明基础数据
     * @param monitoringPointId 监测点 ID
     * @return 包含查询结果的响应对象
     */
    SanMingBasicDataListResponse getSanMingBasicDataByMonitoringPointId(int monitoringPointId);






    /**
     * 按县区分页查询
     * @param pr
     * @param countyDistrict
     * @return
     */
    PageResult<SanMingBasicDataDTO> pageByCounty(PageRequest pr, String countyDistrict);

    /** 按县区获取某字段全部取值（字段名白名单） */
    List<Object> listFieldValuesByCounty(String countyDistrict, SanMingBasicDataField field);

    enum SanMingBasicDataField {
        PlantingArea,PlannedContractAmount,PurchaseQuantity,Describe
    }
    public void debugPrintByCounty(int page, int size, String county);

    List<Object> listFieldValuesByRegion(String countyDistrict, String town, String village,SanMingBasicDataField field);




}
