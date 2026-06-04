package org.example.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SoilData;
import org.example.model.TobaccoLeafData;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * TobaccoLeafDataMapper 接口
 * 用于处理烟草叶片数据的数据库操作
 */
@Mapper
public interface TobaccoLeafDataMapper
{
    /**
     * 插入一条新的烟叶数据。
     *
     * @param tobaccoLeafData 烟叶数据对象
     */
    void insertTobaccoLeafData(TobaccoLeafData tobaccoLeafData);

    /**
     * 获取所有未删除的烟叶数据。
     *
     * @return 未删除的烟叶数据列表
     */
    List<TobaccoLeafData> getAllActiveTobaccoLeafData();

    /**
     * 获取所有烟叶数据。
     *
     * @return 所有烟叶数据列表
     */
    List<TobaccoLeafData> getAllTobaccoLeafData();

    /**
     * 根据 TobaccoDataId 删除烟叶数据（软删除）。
     *
     * @param tobaccoDataId 烟叶数据的 ID
     */
    void deleteTobaccoLeafDataByTobaccoDataId(@Param("tobaccoDataId") int tobaccoDataId);

    /**
     * 根据 TobaccoDataId 恢复已删除的烟叶数据。
     *
     * @param tobaccoDataId 烟叶数据的 ID
     */
    void restoreTobaccoLeafDataByTobaccoDataId(@Param("tobaccoDataId") int tobaccoDataId);

    /**
     * 查询 TobaccoDataId 是否存在。
     *
     * @param tobaccoDataId 烟叶数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean checkTobaccoDataIdExists(@Param("tobaccoDataId") int tobaccoDataId);

    /**
     * 查询 TobaccoDataId 下的数据是否已删除。
     *
     * @param tobaccoDataId 烟叶数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    boolean isTobaccoLeafDataDeleted(@Param("tobaccoDataId") int tobaccoDataId);

    /**
     * 根据 MonitoringPointID 获取所有烟叶数据。
     *
     * @param monitoringPointID 监测点 ID
     * @return 对应监测点的所有烟叶数据列表
     */
    List<TobaccoLeafData> getTobaccoLeafDataByMonitoringPointId(@Param("monitoringPointID") int monitoringPointID);

    /**
     * 根据 TobaccoDataId 更新烟草叶片数据
     *
     * @param tobaccoLeafData 包含要更新的烟草叶片数据的对象，其中应包含 TobaccoDataId 以及其他要更新的字段值
     * @return 受影响的行数
     */
    int updateTobaccoLeafDataByTobaccoDataId(TobaccoLeafData tobaccoLeafData);

    //硬删除
    int hardDeleteExpired(@Param("cutoff") LocalDateTime cutoff);

    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countByCounty(@Param("countyDistrict") String countyDistrict);
    /** 分页查询未删除的数据（可选县区过滤） */
    List<TobaccoLeafData> findByCountyAndPage(@Param("countyDistrict") String countyDistrict,
                                       @Param("limit") int limit,
                                       @Param("offset") int offset);
    List<Object> listFieldValuesByCounty(@Param("countyDistrict") String countyDistrict,
                                         @Param("field") String field);
    //测试用代码
    public void debugPrintByCounty(String countyDistrict);
}
