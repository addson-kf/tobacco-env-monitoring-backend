package org.example.controller;


import org.example.dto.analysis.MaxminDTO;
import org.example.response.common.CommonResponse;
import org.example.service.IMaxminService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/maxmin")
public class MaxminController {
private final IMaxminService service;
public MaxminController(IMaxminService service) {
    this.service = service;
}
    // 土壤
    @GetMapping("/soil")
    public CommonResponse<MaxminDTO> soil(
            @RequestParam String attr,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "period") String periodZh,
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam(required = false) String tobaccoFieldType,
            @RequestParam(required = false, defaultValue = "false") Boolean useEnum
    ) {
        return CommonResponse.ok(service.soilExtrema(attr, year, periodZh, countyDistrict, town, village, tobaccoFieldType, useEnum));
    }

    // 土壤酶
    @GetMapping("/enzyme")
    public CommonResponse<MaxminDTO> enzyme(
            @RequestParam String attr,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "period") String periodZh,
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam(required = false, defaultValue = "false") Boolean useEnum
    ) {
        return CommonResponse.ok(service.enzymeExtrema(attr, year, periodZh, countyDistrict, town, village, useEnum));
    }

    // 烟叶
    @GetMapping("/leaf")
    public CommonResponse<MaxminDTO> leaf(
            @RequestParam String attr,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "period") String periodZh,
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam(required = false, defaultValue = "false") Boolean useEnum
    ) {
        return CommonResponse.ok(service.leafExtrema(attr, year, periodZh, countyDistrict, town, village, useEnum));
    }

    // 天气
    @GetMapping("/weather")
    public CommonResponse<MaxminDTO> weather(
            @RequestParam String attr,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false, name = "period") String periodZh,
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam(required = false, defaultValue = "false") Boolean useEnum
    ) {
        return CommonResponse.ok(service.weatherExtrema(attr, year, periodZh, countyDistrict, town, village, useEnum));
    }
}
