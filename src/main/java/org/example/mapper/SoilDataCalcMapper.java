package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import java.util.Map;

@Mapper
public interface SoilDataCalcMapper {
    List<Map<String,Object>> queryForCalc(
            @Param("tobaccoFieldType") String tobaccoFieldType,
            @Param("countyDistrict")   String countyDistrict,
            @Param("town")             String town,
            @Param("village")          String village,
            @Param("samplingDateFrom") String samplingDateFrom,
            @Param("samplingDateTo")   String samplingDateTo,
            @Param("attributes")       List<String> attributes
    );
}
