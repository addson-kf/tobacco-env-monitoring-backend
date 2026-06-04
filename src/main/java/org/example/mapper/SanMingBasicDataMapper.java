package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SanMingBasicData;
import org.example.model.SoilData;

import java.util.List;

@Mapper
public interface SanMingBasicDataMapper
{
    /**
     * 插入一条新的三明基础数据。
     *
     * @param sanMingBasicData 三明基础数据对象
     * @return 插入成功的记录数
     */
    int insertSanMingBasicData(SanMingBasicData sanMingBasicData);

    /**
     * 获取所有未删除的三明基础数据。
     *
     * @return 未删除的三明基础数据列表
     */
    List<SanMingBasicData> getAllActiveSanMingBasicData();

    /**
     * 获取所有三明基础数据。
     *
     * @return 所有三明基础数据列表
     */
    List<SanMingBasicData> getAllSanMingBasicData();

    /**
     * 根据 id 删除三明基础数据（软删除）。
     *
     * @param id 三明基础数据的 ID
     * @return 更新成功的记录数
     */
    int deleteSanMingBasicDataById(@Param("id") int id);

    /**
     * 根据 id 恢复已删除的三明基础数据。
     *
     * @param id 三明基础数据的 ID
     * @return 更新成功的记录数
     */
    int restoreSanMingBasicDataById(@Param("id") int id);

    /**
     * 查询 id 是否存在。
     *
     * @param id 三明基础数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean checkIdExists(@Param("id") int id);

    /**
     * 查询 id 下的数据是否已删除。
     *
     * @param id 三明基础数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    boolean isSanMingBasicDataDeleted(@Param("id") int id);

    /**
     * 根据 id 修改三明基础数据。
     *
     * @param sanMingBasicData 包含要更新信息的三明基础数据对象
     * @return 更新成功的记录数
     */
    int updateSanMingBasicDataById(SanMingBasicData sanMingBasicData);

    /**
     * 根据监测点 ID 查询未删除的三明基础数据
     * @param monitoringPointId 监测点 ID
     * @return 符合条件的三明基础数据列表
     */
    List<SanMingBasicData> getSanMingBasicDataByMonitoringPointId(@Param("monitoringPointId") int monitoringPointId);


//硬删除
int hardDeleteExpired(@Param("cutoff") java.time.LocalDateTime cutoff);


    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countByCounty(@Param("countyDistrict") String countyDistrict);
    /** 分页查询未删除的数据（可选县区过滤） */
    List<SanMingBasicData> findByCountyAndPage(@Param("countyDistrict") String countyDistrict,
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
