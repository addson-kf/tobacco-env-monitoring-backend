package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.model.analysis.CorrelationXY;

import java.util.List;
import java.util.Map;
import java.util.Set;


@Mapper
public interface AnalysisMapper {

    //相关系数
    List<CorrelationXY> soilPairs(@Param("attrX") String attrX,
                                  @Param("attrY") String attrY,
                                  @Param("year") Integer year,
                                  @Param("period") String period,          // 英文归一化后
                                  @Param("countyDistrict") String countyDistrict,
                                  @Param("town") String town,
                                  @Param("village") String village);

    List<CorrelationXY> enzymePairs(@Param("attrX") String attrX,
                                    @Param("attrY") String attrY,
                                    @Param("year") Integer year,
                                    @Param("period") String period,
                                    @Param("countyDistrict") String countyDistrict,
                                    @Param("town") String town,
                                    @Param("village") String village);

    List<CorrelationXY> leafPairs(@Param("attrX") String attrX,
                                  @Param("attrY") String attrY,
                                  @Param("year") Integer year,
                                  @Param("period") String period,
                                  @Param("countyDistrict") String countyDistrict,
                                  @Param("town") String town,
                                  @Param("village") String village);

    List<CorrelationXY> weatherPairs(@Param("attrX") String attrX,
                                     @Param("attrY") String attrY,
                                     @Param("year") Integer year,
                                     @Param("period") String period,
                                     @Param("countyDistrict") String countyDistrict,
                                     @Param("town") String town,
                                     @Param("village") String village);

    //平均数
    // 平均值：返回 Map，包含键 "n" 和 "mean"
    Map<String, Object> meanSoil(@Param("attr") String attr,
                                 @Param("year") Integer year,
                                 @Param("period") String period,            // 归一化后的英文
                                 @Param("countyDistrict") String countyDistrict,
                                 @Param("town") String town,
                                 @Param("village") String village,
                                 @Param("tobaccoFieldType") String tobaccoFieldType);

    Map<String, Object> meanEnzyme(@Param("attr") String attr,
                                   @Param("year") Integer year,
                                   @Param("period") String period,
                                   @Param("countyDistrict") String countyDistrict,
                                   @Param("town") String town,
                                   @Param("village") String village
                                   );

    Map<String, Object> meanLeaf(@Param("attr") String attr,
                                 @Param("year") Integer year,
                                 @Param("period") String period,
                                 @Param("countyDistrict") String countyDistrict,
                                 @Param("town") String town,
                                 @Param("village") String village);

    Map<String, Object> meanWeather(@Param("attr") String attr,
                                    @Param("year") Integer year,
                                    @Param("period") String period,
                                    @Param("countyDistrict") String countyDistrict,
                                    @Param("town") String town,
                                    @Param("village") String village);
    /** 新增吸味感官 */
    Map<String,Object> meanSmoking(@Param("attr") String attr, @Param("year") Integer year,
                                   @Param("period") String period, @Param("countyDistrict") String county,
                                   @Param("town") String town, @Param("village") String village);

    List<CorrelationXY> smokingPairs(@Param("attrX") String attrX, @Param("attrY") String attrY,
                                     @Param("year") Integer year, @Param("period") String period,
                                     @Param("countyDistrict") String county, @Param("town") String town,
                                     @Param("village") String village);
}
