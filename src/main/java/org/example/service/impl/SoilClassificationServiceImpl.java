package org.example.service.impl;
import lombok.RequiredArgsConstructor;
import org.example.dao.impl.SoilClassificationDaoImpl;
import org.example.dto.SoilClassificationListDTO.SoilClassificationListDTO;
import org.example.dto.SoilClassificationListDTO.SoilClassificationRuleDTO;
import org.example.dto.UpdateSoilClassificationListDTO.UpdateSoilClassificationListDTO;
import org.example.mapper.SoilClassificationMapper;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.SoilQuality;
import org.example.response.common.Page.PagingHelper;
import org.example.response.common.SoilClassification.SoilClassificationResponse;
import org.example.response.base.UpdateResponse;
import org.example.model.SoilClassificationRule;
import org.example.service.DataStorageService;
import org.example.service.ISoilClassificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.example.service.IMonitoringPointService;
import org.example.service.ISoilDataService;
import org.example.response.common.CommonResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SoilClassificationServiceImpl implements ISoilClassificationService
{
    @Autowired
    private SoilClassificationDaoImpl soilClassificationDao;

    @Autowired
    private SoilDataServiceImpl soilDataService;


    /**
     * 查找所有土壤分类规则，并以表格形式输出到控制台。
     * 1. 调用 soilClassificationDao 获取所有土壤分类规则。
     * 2. 定义表格的宽度和格式，输出表头和数据行。
     */
    @Override
    @Transactional
    public void findAllSoilClassificationRules() {
        List<SoilClassificationRule> rules = soilClassificationDao.getAllSoilClassificationRules();

        // 输出表格
        int attributeWidth = 30;
        int minValueWidth = 10;
        int maxValueWidth = 10;
        int classificationWidth = 15;
        int totalWidth = attributeWidth + minValueWidth + maxValueWidth + classificationWidth + 15;

        String tableName = " Soil Classification Rules ";
        System.out.println("+" + "-".repeat(totalWidth - 2) + "+");
        System.out.printf("|%s%s%s|\n",
                " ".repeat((totalWidth - tableName.length()) / 2),
                tableName,
                " ".repeat((totalWidth - tableName.length()) / 2 - 1));
        System.out.println("+" + "-".repeat(totalWidth - 2) + "+");

        String headerFormat = "| %-30s | %-10s | %-10s | %-15s |\n";
        System.out.printf(headerFormat, "Soil Attribute", "Min Value", "Max Value", "Classification");
        System.out.println("+" + "-".repeat(totalWidth - 2) + "+");

        for (SoilClassificationRule rule : rules) {
            System.out.printf(headerFormat,
                    rule.getSoilAttribute(),
                    rule.getMinValue(),
                    rule.getMaxValue(),
                    rule.getClassification());
        }

        System.out.println("+" + "-".repeat(totalWidth - 2) + "+");
    }

    /**
     * 根据给定的土壤属性和数值，获取对应的土壤质量等级。
     * 1. 调用 soilClassificationDao 根据属性和数值查找对应的土壤分类规则。
     * 2. 如果找到规则，返回其土壤质量等级；否则返回 null。
     *
     * @param attribute 土壤属性
     * @param value     土壤属性的数值
     * @return 对应的土壤质量等级，找不到规则时返回 null
     */
    @Override
    @Transactional
    public SoilQuality getSoilQuality(SoilAttribute attribute, double value) {
        SoilClassificationRule rule = soilClassificationDao.getSoilClassificationRuleByAttributeAndValue(attribute, value);
        return rule != null ? rule.getClassification() : null;
    }

    /**
     * 更新土壤分类规则。
     * 1. 调用 soilClassificationDao 删除指定土壤属性的旧规则。
     * 2. 调用 soilClassificationDao 批量插入新的土壤分类规则。
     * 3. 如果插入的行数与新规则的数量不一致，抛出运行时异常。
     * 4. 调用 soilDataService 更新特定属性的分类。
     * 5. 返回更新操作的响应结果。
     *
     * @param dto 包含要更新的土壤属性和新规则的 DTO 对象
     * @return 更新操作的响应结果，包含更新状态和消息
     */
    @Override
    @Transactional
    public UpdateResponse updateSoilClassification(UpdateSoilClassificationListDTO dto) {
        try {
            // 删除旧规则
            soilClassificationDao.deleteBySoilAttribute(dto.getSoilAttribute().toString());

            // 批量插入新规则
            soilClassificationDao.batchInsert(dto.getSoilAttribute().toString(), dto.getRules());

            soilDataService.updateClassificationForSpecificAttribute(dto.getSoilAttribute().toString());

            return new UpdateResponse(true, "分类规则更新成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new UpdateResponse(false, "更新失败：" + e.getMessage());
        }
    }

    /**
     * 获取所有土壤分类规则，并封装成响应对象返回。
     * 1. 调用 soilClassificationDao 获取所有土壤分类规则。
     * 2. 将规则转换为 SoilClassificationRuleDTO 列表。
     * 3. 将规则 DTO 列表封装成 SoilClassificationListDTO。
     * 4. 根据是否有规则数据，构造相应的 SoilClassificationResponse 返回。
     *
     * @return 包含土壤分类规则数据和状态信息的响应对象
     */
    @Override
    public SoilClassificationResponse getAllSoilClassificationRules() {
        List<SoilClassificationRule> rules = soilClassificationDao.getAllSoilClassificationRules();
        List<SoilClassificationRuleDTO> ruleDTOs = rules.stream()
                .map(rule -> new SoilClassificationRuleDTO(
                        rule.getSoilAttribute(),
                        rule.getMinValue(),
                        rule.getMaxValue(),
                        rule.getClassification()))
                .collect(Collectors.toList());

        SoilClassificationListDTO listDTO = new SoilClassificationListDTO(ruleDTOs);

        if (listDTO.getSoilClassificationRules().isEmpty()) {
            return new SoilClassificationResponse(false, "没有找到分类规则", null);
        }

        return new SoilClassificationResponse(true, "查询成功", listDTO);
    }







}