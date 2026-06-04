package org.example.controller;

import org.example.dto.SmokingEvaluationDataListDTO.SmokingEvaluationDataDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SmokingEvaluationData.SmokingEvaluationDataListResponse;
import org.example.service.ISmokingEvaluationDataService;
import org.example.service.ISoilDataService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.example.response.common.CommonResponse;
import lombok.RequiredArgsConstructor;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 吸烟评估数据控制器，处理与吸烟评估数据相关的请求。
 */
@RestController
@RequestMapping("/api/smokingEvaluationData")
@CrossOrigin(origins = "*")
public class SmokingEvaluationDataController {

    @Autowired
    private ISmokingEvaluationDataService smokingEvaluationDataService;

    private static final Logger log = LoggerFactory.getLogger(SmokingEvaluationDataController.class);

    /**
     * 插入新吸烟评估数据
     */
    @PostMapping("#{@apiConfig.smokingEvaluationData.endpoints.insert}")
    public InsertResponse insertSmokingEvaluationData(@RequestBody SmokingEvaluationDataDTO smokingEvaluationDataDTO) {
        return smokingEvaluationDataService.insertSmokingEvaluationData(smokingEvaluationDataDTO);
    }

    /**
     * 逻辑删除吸烟评估数据
     */
    @DeleteMapping("#{@apiConfig.smokingEvaluationData.endpoints.delete}")  // /delete/{id}
    public DeleteResponse deleteSmokingEvaluationDataById(@PathVariable("smokingEvaluationId") int smokingEvaluationId) {
        return smokingEvaluationDataService.deleteSmokingEvaluationDataById(smokingEvaluationId);
    }

    /**
     * 修改吸烟评估数据
     */
    @PostMapping("#{@apiConfig.smokingEvaluationData.endpoints.update}")
    public UpdateResponse updateSmokingEvaluationDataById(@RequestBody SmokingEvaluationDataDTO smokingEvaluationData) {
        return smokingEvaluationDataService.updateSmokingEvaluationDataById(smokingEvaluationData);
    }

    /**
     * 恢复吸烟评估数据
     */
    @PostMapping("#{@apiConfig.smokingEvaluationData.endpoints.restore}")   // /restore/{id}
    public UpdateResponse restoreSmokingEvaluationDataById(@PathVariable("smokingEvaluationId") int smokingEvaluationId) {
        return smokingEvaluationDataService.restoreSmokingEvaluationDataById(smokingEvaluationId);
    }

    /**
     * 查询未删除的吸烟评估数据
     */
    @GetMapping("#{@apiConfig.smokingEvaluationData.endpoints.allActive}")
    public SmokingEvaluationDataListResponse getAllActiveSmokingEvaluationData() {
        try {
            return smokingEvaluationDataService.getAllActiveSmokingEvaluationData();
        } catch (Exception e) {
            log.error("[/smokingEvaluationData/allActive] 查询失败", e);
            String msg = "查询失败: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            return new SmokingEvaluationDataListResponse(false, msg, null);
        }
    }

    /**
     * 查询所有吸烟评估数据（含已删除）
     */
    @GetMapping("#{@apiConfig.smokingEvaluationData.endpoints.all}")
    public SmokingEvaluationDataListResponse getAllSmokingEvaluationData() {
        try {
            return smokingEvaluationDataService.getAllSmokingEvaluationData();
        } catch (Exception e) {
            log.error("[/smokingEvaluationData/all] 查询失败", e);
            String msg = "查询失败: " + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName());
            return new SmokingEvaluationDataListResponse(false, msg, null);
        }
    }

    /**
     * 按监测点ID查询吸烟评估数据
     */
    @GetMapping("#{@apiConfig.smokingEvaluationData.endpoints.byMonitoringPointId}")
    public SmokingEvaluationDataListResponse getSmokingEvaluationDataByMonitoringPointId(@PathVariable("monitoringPointId") int monitoringPointId) {
        return smokingEvaluationDataService.getSmokingEvaluationDataByMonitoringPointId(monitoringPointId);
    }

    /**
     * 按县区查询数据
     * @param countyDistrict
     * @param page
     * @param size
     * @return
     */
    @GetMapping("/by-county")
    public PageResult<SmokingEvaluationDataDTO> pageByCounty(
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pr =  new PageRequest();
        pr.setPage(page);
        pr.setSize(size);
        return smokingEvaluationDataService.pageByCounty(pr, countyDistrict);
    }

    /** 按县区获取指定字段所有取值（白名单） */
    @GetMapping("/field-describes")
    public List<Object> listFieldValues(@RequestParam(required = false) String county,
                                        @RequestParam ISmokingEvaluationDataService.ISmokingEvaluationDatafield field) {
        return smokingEvaluationDataService.listFieldValuesByCounty(county, field);
    }

    /** 按县/乡/村获取指定字段所有取值（字段白名单） */
    @GetMapping("/field-describes/by-region")
    public List<Object> listFieldValuesByRegion(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam ISmokingEvaluationDataService.ISmokingEvaluationDatafield field
    ) {
        return smokingEvaluationDataService.listFieldValuesByRegion(county, town, village, field);
    }

}