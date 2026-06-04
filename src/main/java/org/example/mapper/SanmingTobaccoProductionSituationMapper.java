package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.SanmingTobaccoProductionSituation;

import java.util.List;

@Mapper
public interface SanmingTobaccoProductionSituationMapper {
    int update(SanmingTobaccoProductionSituation dto);
    int add(SanmingTobaccoProductionSituation dto);

    SanmingTobaccoProductionSituation findOne(@Param("year") Integer year,
                                              @Param("county") String county,
                                              @Param("town") String town,
                                              @Param("village") String village);

    List<SanmingTobaccoProductionSituation> list(@Param("year") Integer year,
                                                 @Param("county") String county,
                                                 @Param("town") String town,
                                                 @Param("village") String village);

    long countByFilter(@Param("year") Integer year,
                       @Param("county") String county,
                       @Param("town") String town,
                       @Param("village") String village);

    List<SanmingTobaccoProductionSituation> pageByFilter(@Param("year") Integer year,
                                                         @Param("county") String county,
                                                         @Param("town") String town,
                                                         @Param("village") String village,
                                                         @Param("offset") int offset,
                                                         @Param("size") int size);

    int delete(@Param("year") Integer year,
               @Param("countyDistrict") String countyDistrict,
               @Param("town") String town,
               @Param("village") String village);
}
