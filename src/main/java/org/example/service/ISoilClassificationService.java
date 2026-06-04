package org.example.service;

import org.example.dto.SoilClassificationListDTO.SoilClassificationRuleDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.UpdateSoilClassificationListDTO.UpdateSoilClassificationListDTO;
import org.example.response.common.SoilClassification.SoilClassificationResponse;
import org.example.response.base.UpdateResponse;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.SoilQuality;

import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;

import java.util.List;


public interface ISoilClassificationService
{
    /**
     * 查询所有的土壤分类规则，并在控制台输出表格。
     */
    void findAllSoilClassificationRules();

    /**
     * 根据 SoilAttribute 和数值获取 SoilQuality
     *
     * @param attribute 土壤属性
     * @param value 土壤值
     * @return SoilQuality
     */
    SoilQuality getSoilQuality(SoilAttribute attribute, double value);

    /**
     * 更新土壤分类规则
     *
     * @param dto 更新土壤分类规则的DTO
     * @return 更新响应
     */
    UpdateResponse updateSoilClassification(UpdateSoilClassificationListDTO dto);

    /**
     * 获取所有土壤分类规则
     *
     * @return 土壤分类规则响应
     */
    SoilClassificationResponse getAllSoilClassificationRules();






}
