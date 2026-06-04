package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SoilData;
import org.example.model.SoilEnzymeActivityData;

import java.time.LocalDateTime;
import java.util.List;

/**
 * SoilEnzymeActivityDataMapper接口
 * 用于处理这玩意的数据库操作
 */
@Mapper
public interface SoilEnzymeActivityDataMapper
{
    /**
     * 插入土壤酶活性数据
     * @param soilEnzymeActivityData 土壤酶活性数据
     * @return 插入操作影响的行数
     */
    int insertSoilEnzymeActivityData(SoilEnzymeActivityData soilEnzymeActivityData);

    /**
     * 获取所有非删除的土壤酶活性数据
     * @return 土壤酶活性数据列表
     */
    List<SoilEnzymeActivityData> getAllActiveSoilEnzymeActivityData();

    /**
     * 获取所有土壤酶活性数据
     * @return 所有土壤酶活性数据列表
     */
    List<SoilEnzymeActivityData> getAllSoilEnzymeActivityData();

    /**
     * 根据 SoilEnzymeActivityId 删除土壤酶活性数据
     * @param soilEnzymeActivityId 土壤酶活性数据 ID
     * @return 更新操作影响的行数
     */
    int deleteSoilEnzymeActivityDataById(int soilEnzymeActivityId);

    /**
     * 根据 SoilEnzymeActivityId 恢复土壤酶活性数据
     * @param soilEnzymeActivityId 土壤酶活性数据 ID
     * @return 更新操作影响的行数
     */
    int restoreSoilEnzymeActivityDataById(int soilEnzymeActivityId);

    /**
     * 查询 SoilEnzymeActivityId 是否存在
     * @param soilEnzymeActivityId 土壤酶活性数据 ID
     * @return 是否存在
     */
    boolean checkSoilEnzymeActivityIdExists(int soilEnzymeActivityId);

    /**
     * 查询 SoilEnzymeActivityId 下的数据是否已删除
     * @param soilEnzymeActivityId 土壤酶活性数据 ID
     * @return 数据是否已删除
     */
    boolean isSoilEnzymeActivityDataDeleted(int soilEnzymeActivityId);

    /**
     * 根据 MonitoringPointId 查询土壤酶活性数据
     * @param monitoringPointId 监测点 ID
     * @return 土壤酶活性数据列表
     */
    List<SoilEnzymeActivityData> getSoilEnzymeActivityDataByMonitoringPointId(int monitoringPointId);

    int updateSoilEnzymeActivityDataById(SoilEnzymeActivityData soilEnzymeActivityData);


//硬删除
int hardDeleteExpired(@Param("cutoff") LocalDateTime cutoff);


    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countByCounty(@Param("countyDistrict") String countyDistrict);
    /** 分页查询未删除的数据（可选县区过滤） */
    List<SoilEnzymeActivityData> findByCountyAndPage(@Param("countyDistrict") String countyDistrict,
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
