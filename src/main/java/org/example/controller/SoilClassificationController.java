package org.example.controller;

import org.example.dto.SoilClassificationListDTO.SoilClassificationListDTO;
import org.example.dto.SoilClassificationListDTO.SoilClassificationRuleDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.UpdateSoilClassificationListDTO.UpdateSoilClassificationListDTO;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.SoilQuality;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SoilClassification.SoilClassificationResponse;
import org.example.service.ISoilClassificationService;
import org.example.service.ISoilDataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("#{apiConfig.soilClassification.basePath}")  // 动态注入基础路径
@CrossOrigin(origins = "*")
public class SoilClassificationController {

    @Autowired
    private ISoilClassificationService soilClassificationService;

    /**
     * 获取所有土壤分类规则
     */
    @GetMapping("#{apiConfig.soilClassification.endpoints.rules}")
    public SoilClassificationResponse getAllSoilClassificationRules() {
        return soilClassificationService.getAllSoilClassificationRules();
    }

    /**
     * 根据属性和数值查询土壤质量等级
     */
    @GetMapping("#{@apiConfig.soilClassification.endpoints.quality}")
    public SoilQuality getSoilQuality(@RequestParam("attribute") SoilAttribute attribute, @RequestParam("value") double value) {
        return soilClassificationService.getSoilQuality(attribute, value);
    }

    /**
     * 更新土壤分类规则
     */
    @PostMapping("#{@apiConfig.soilClassification.endpoints.update}")
    public UpdateResponse updateSoilClassification(@RequestBody UpdateSoilClassificationListDTO dto) {
        return soilClassificationService.updateSoilClassification(dto);
    }

    /**
     * 全量查询并输出规则
     */
    @GetMapping("#{@apiConfig.soilClassification.endpoints.findAll}")
    public String findAllSoilClassificationRules() {
        soilClassificationService.findAllSoilClassificationRules();
        return "所有土壤分类规则已输出到控制台。";
    }






}