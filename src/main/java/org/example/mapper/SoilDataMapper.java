package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SoilData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SoilDataMapper 接口
 * 用于处理土壤数据的数据库操作
 */
@Mapper
public interface SoilDataMapper
{
    /**
     * 根据 ID 查询土壤数据
     * @param soilDataID 土壤数据 ID
     * @return 土壤数据对象
     */
    SoilData findById(int soilDataID);

    /**
     * 查询所有土壤数据（仅包含未删除的数据）
     * @return 土壤数据列表
     */
    List<SoilData> findAll();

    /**
     * 查询所有土壤数据（包括已删除的）
     * @return 土壤数据列表
     */
    List<SoilData> findAllForAdmin();

    /**
     * 插入一条新的土壤数据记录
     * @param soilData 土壤数据对象
     * @return 插入操作影响的行数
     */
    int insertWithClassification(SoilData soilData);

    /**
     * 软删除指定 ID 的土壤数据
     * @param soilDataID 土壤数据 ID
     * @return 更新操作影响的行数
     */
    int softDeleteById(int soilDataID);

    /**
     * 恢复已删除的土壤数据
     * @param soilDataID 土壤数据 ID
     * @return 更新操作影响的行数
     */
    int restoreById(int soilDataID);

    /**
     * 更新指定 ID 的土壤数据并自动更新分类信息
     * @param soilData 土壤数据对象
     * @return 更新操作影响的行数
     */
    int updateDataWithClassification(SoilData soilData);

    /**
     * 修改某一种类的所有记录的值的分类
     * @param soilAttribute 土壤属性
     * @return 更新操作影响的行数
     */
    int updateClassificationForSpecificAttribute(String soilAttribute);

    // 查询 SoilDataID 是否存在
    boolean existsById(Long soilDataID);

    // 查询 SoilDataID 是否被删除
    boolean isDeletedById(Long soilDataID);


    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countByCounty(@Param("countyDistrict") String countyDistrict);
    /** 分页查询未删除的数据（可选县区过滤） */
    List<SoilData> findByCountyAndPage(@Param("countyDistrict") String countyDistrict,
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
    //硬删除
    int hardDeleteExpired(@Param("cutoff") LocalDateTime cutoff);

}
