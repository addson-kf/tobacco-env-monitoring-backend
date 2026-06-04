package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.dto.UpdateSoilClassificationListDTO.UpdateSoilClassificationDTO;
import org.example.model.SoilData;
import org.example.model.enums.SoilAttribute;
import org.example.model.SoilClassificationRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SoilClassificationMapper接口
 *
 */
@Mapper
public interface SoilClassificationMapper
{
    // 查询所有分类规则
    List<SoilClassificationRule> getAllSoilClassificationRules();

    // 根据 SoilAttribute 枚举和数值获取对应的 SoilQuality
    SoilClassificationRule getSoilClassificationRuleByAttributeAndValue(
            @Param("attribute") SoilAttribute attribute,
            @Param("value") double value
    );

    // 更新表内数据，需要删除旧的记录并批量插入新的数据
    int deleteBySoilAttribute(@Param("soilAttribute") String soilAttribute);

    // 批量插入 SoilClassificationRules
    int batchInsert(@Param("soilAttribute") String soilAttribute,
                    @Param("rules") List<UpdateSoilClassificationDTO> rules);





}
