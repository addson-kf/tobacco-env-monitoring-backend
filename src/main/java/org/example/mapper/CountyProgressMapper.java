package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.model.CountyProgress;

import java.util.List;
import java.util.Map;

@Mapper
public interface CountyProgressMapper {

    int insert(CountyProgress cp);

    int updateById(CountyProgress cp); // 根据 CountyId 有选择地更新

    // 推荐：UPSERT，存在则更新 Progress（或你需要更新的字段）
    int upsert(CountyProgress cp);

    CountyProgress findOne(@Param("county") String county,
                           @Param("town") String town,
                           @Param("village") String village);

    List<CountyProgress> list(@Param("county") String county,
                              @Param("town") String town,
                              @Param("village") String village);

    long countByFilter(@Param("county") String county,
                       @Param("town") String town,
                       @Param("village") String village);

    List<CountyProgress> pageByFilter(@Param("county") String county,
                                      @Param("town") String town,
                                      @Param("village") String village,
                                      @Param("offset") int offset,
                                      @Param("size") int size);


    /** 一条 SQL：按县/镇 + 年份 + 月份列表 重算并回写 CountyProgress.Progress */
    int recomputeProgress(@Param("county") String county,
                          @Param("town") String town,
                          @Param("year") Integer year,
                          @Param("months") List<Integer> months,
                          @Param("weatherRequired") int weatherRequired,
                          @Param("wSoil") double wSoil,
                          @Param("wEnzyme") double wEnzyme,
                          @Param("wLeaf") double wLeaf,
                          @Param("wSmoke") double wSmoke,
                          @Param("wWeather") double wWeather,
                          @Param("smokeMpCol") String smokeMpCol); // 兼容 SmokingEvaluationDatas 的 MonitoringPointId


    List<Map<String, Object>> debugTableColumns();


}


