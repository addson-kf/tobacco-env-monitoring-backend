package org.example.mapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface MaxminMapper {
    // SoilDatas
    Double soilMax(@Param("attr") String attr,
                   @Param("year") Integer year,
                   @Param("periodZh") String periodZh,
                   @Param("countyDistrict") String countyDistrict,
                   @Param("town") String town,
                   @Param("village") String village,
                   @Param("tobaccoFieldType") String tobaccoFieldType,
                   @Param("useEnum") Boolean useEnum);

    Double soilMin(@Param("attr") String attr,
                   @Param("year") Integer year,
                   @Param("periodZh") String periodZh,
                   @Param("countyDistrict") String countyDistrict,
                   @Param("town") String town,
                   @Param("village") String village,
                   @Param("tobaccoFieldType") String tobaccoFieldType,
                   @Param("useEnum") Boolean useEnum);

    // SoilEnzymeActivityDatas
    Double enzymeMax(@Param("attr") String attr,
                     @Param("year") Integer year,
                     @Param("periodZh") String periodZh,
                     @Param("countyDistrict") String countyDistrict,
                     @Param("town") String town,
                     @Param("village") String village,
                     @Param("useEnum") Boolean useEnum);

    Double enzymeMin(@Param("attr") String attr,
                     @Param("year") Integer year,
                     @Param("periodZh") String periodZh,
                     @Param("countyDistrict") String countyDistrict,
                     @Param("town") String town,
                     @Param("village") String village,
                     @Param("useEnum") Boolean useEnum);

    // TobaccoLeafDatas
    Double leafMax(@Param("attr") String attr,
                   @Param("year") Integer year,
                   @Param("periodZh") String periodZh,
                   @Param("countyDistrict") String countyDistrict,
                   @Param("town") String town,
                   @Param("village") String village,

                   @Param("useEnum") Boolean useEnum);

    Double leafMin(@Param("attr") String attr,
                   @Param("year") Integer year,
                   @Param("periodZh") String periodZh,
                   @Param("countyDistrict") String countyDistrict,
                   @Param("town") String town,
                   @Param("village") String village,
                   @Param("useEnum") Boolean useEnum);

    // WeatherDatas
    Double weatherMax(@Param("attr") String attr,
                      @Param("year") Integer year,
                      @Param("periodZh") String periodZh,
                      @Param("countyDistrict") String countyDistrict,
                      @Param("town") String town,
                      @Param("village") String village,
                      @Param("useEnum") Boolean useEnum);

    Double weatherMin(@Param("attr") String attr,
                      @Param("year") Integer year,
                      @Param("periodZh") String periodZh,
                      @Param("countyDistrict") String countyDistrict,
                      @Param("town") String town,
                      @Param("village") String village,
                      @Param("useEnum") Boolean useEnum);

}
