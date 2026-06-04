package org.example.controller;

import org.example.dto.analysis.CorrelationResultDTO;
import org.example.dto.analysis.MeanResultDTO;
import org.example.response.common.CommonResponse;
import org.example.service.IAnalysisService;
import org.springframework.web.bind.annotation.*;
import org.example.dto.analysis.MaxminDTO;


@RestController
@RequestMapping("/api/analysis")
public class AnalysisController {
    private final IAnalysisService analysisService;

    public AnalysisController(IAnalysisService analysisService) {
        this.analysisService = analysisService;
    }

    // 示例：/api/analysis/correlation?dataset=soil&attrX=PH&attrY=有机质&year=2024&period=种烟前&countyDistrict=宁化县
    @GetMapping("/correlation")
    public CommonResponse<CorrelationResultDTO> correlation(
            @RequestParam String dataset,
            @RequestParam String attrX,
            @RequestParam String attrY,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "period") String periodZh,
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village
    ) {
        // 直接调用服务，不传递分页参数
        return CommonResponse.ok(
                analysisService.correlation(dataset, attrX, attrY, year, periodZh, countyDistrict, town, village)
        );
    }

    // 平均数
    @GetMapping("/mean")
    public MeanResultDTO mean(@RequestParam String dataset,
                              @RequestParam String attr,
                              @RequestParam(required = false) Integer year,   // 不传=所有年份
                              @RequestParam(required = false) String period,  // 可中文/英文
                              @RequestParam(required = false) String countyDistrict,
                              @RequestParam(required = false) String town,
                              @RequestParam(required = false) String village,
                              @RequestParam(required = false) String tobaccoFieldType
    ) {
        // 直接调用服务，不传递分页参数
        return analysisService.mean(dataset, attr, year, period, countyDistrict, town, village, tobaccoFieldType);
    }

    // ✅ 新增：土壤最大值 / 最小值
    @GetMapping("/soil/maxmin")
    public MaxminDTO soilMaxmin(@RequestParam String attr,
                                @RequestParam(required = false) String tobaccoFieldType,
                                @RequestParam(required = false) String countyDistrict,
                                @RequestParam(required = false) String town,
                                @RequestParam(required = false) String village) {
        return analysisService.soilMaxmin(attr, tobaccoFieldType, countyDistrict, town, village);
    }
}