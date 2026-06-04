package org.example.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SmokingEvaluationData;
import org.example.model.SoilData;

import java.time.LocalDateTime;
import java.util.List;
@Mapper
/**
 * SmokingEvaluationDataMapper 接口，用于操作吸烟评估数据。
 */
public interface SmokingEvaluationDataMapper
{
    /**
     * 插入一条新的吸烟评估数据。
     *
     * @param smokingEvaluationData 吸烟评估数据对象
     */
    void insertSmokingEvaluationData(SmokingEvaluationData smokingEvaluationData);

    /**
     * 获取所有未删除的吸烟评估数据。
     *
     * @return 未删除的吸烟评估数据列表
     */
    List<SmokingEvaluationData> getAllActiveSmokingEvaluationData();

    /**
     * 获取所有吸烟评估数据。
     *
     * @return 所有吸烟评估数据列表
     */
    List<SmokingEvaluationData> getAllSmokingEvaluationData();

    /**
     * 根据 SmokingEvaluationId 删除吸烟评估数据（软删除）。
     *
     * @param smokingEvaluationId 吸烟评估数据的 ID
     */
    void deleteSmokingEvaluationDataById(@Param("smokingEvaluationId") int smokingEvaluationId);

    /**
     * 根据 SmokingEvaluationId 恢复已删除的吸烟评估数据。
     *
     * @param smokingEvaluationId 吸烟评估数据的 ID
     */
    void restoreSmokingEvaluationDataById(@Param("smokingEvaluationId") int smokingEvaluationId);

    /**
     * 查询 SmokingEvaluationId 是否存在。
     *
     * @param smokingEvaluationId 吸烟评估数据的 ID
     * @return 如果存在返回 true，否则返回 false
     */
    boolean checkSmokingEvaluationIdExists(@Param("smokingEvaluationId") int smokingEvaluationId);

    /**
     * 查询 SmokingEvaluationId 下的数据是否已删除。
     *
     * @param smokingEvaluationId 吸烟评估数据的 ID
     * @return 如果已删除返回 true，否则返回 false
     */
    boolean isSmokingEvaluationDataDeleted(@Param("smokingEvaluationId") int smokingEvaluationId);

    /**
     * 根据 MonitoringPointID 获取所有吸烟评估数据。
     *
     * @param monitoringPointId 监测点 ID
     * @return 对应监测点的所有吸烟评估数据列表
     */
    List<SmokingEvaluationData> getSmokingEvaluationDataByMonitoringPointId(@Param("monitoringPointId") int monitoringPointId);

    /**
     * 根据 SmokingEvaluationId 更新吸烟评估数据
     * @param smokingEvaluationData 包含更新信息的吸烟评估数据对象
     * @return 更新成功的记录数
     */
    int updateSmokingEvaluationDataById(SmokingEvaluationData smokingEvaluationData);

//硬删除
int hardDeleteExpired(@Param("cutoff") LocalDateTime cutoff);

    /** 统计未删除(IsDelete=false)且(可选)按县区过滤的总数 */
    long countByCounty(@Param("countyDistrict") String countyDistrict);
    /** 分页查询未删除的数据（可选县区过滤） */
    List<SmokingEvaluationData> findByCountyAndPage(@Param("countyDistrict") String countyDistrict,
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
