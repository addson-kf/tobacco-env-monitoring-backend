package org.example.controller;
import org.example.dto.SanmingTobaccoProductionSituationDTO.SanmingTobaccoProductionSituationDTO;
import org.example.dto.SanmingTobaccoProductionSituationDTO.SanmingTobaccoProductionSituationListDTO;
import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageResult;
import org.example.service.ISanmingTobaccoProductionSituationService;
import org.springframework.web.bind.annotation.*;
import org.example.model.SanmingTobaccoProductionSituation;

import java.util.List;
@RestController
@RequestMapping("/api/sanming-tobacco-production-situation")
public class SanmingTobaccoProductionSituationController {
    private final ISanmingTobaccoProductionSituationService service;

    public SanmingTobaccoProductionSituationController(ISanmingTobaccoProductionSituationService service) {
        this.service = service;
    }

    /** 单条：按 year/county/town/village */
    @GetMapping("/one")
    public CommonResponse<SanmingTobaccoProductionSituationDTO> one(@RequestParam Integer year,
                                                                    @RequestParam String county,
                                                                    @RequestParam String town,
                                                                    @RequestParam String village) {
        return CommonResponse.ok(service.findOne(year, county, town, village));
    }

    /** 列表：可选过滤 */
    @GetMapping
    public CommonResponse<SanmingTobaccoProductionSituationListDTO> list(@RequestParam(required = false) Integer year,
                                                                         @RequestParam(required = false) String county,
                                                                         @RequestParam(required = false) String town,
                                                                         @RequestParam(required = false) String village) {
        List<SanmingTobaccoProductionSituationDTO> list = service.list(year, county, town, village);
        return CommonResponse.ok(new SanmingTobaccoProductionSituationListDTO(list));
    }

    /** 分页：可选过滤 */
    @GetMapping("/page")
    public CommonResponse<PageResult<SanmingTobaccoProductionSituationDTO>> page(@RequestParam(required = false) Integer year,
                                                                                 @RequestParam(required = false) String county,
                                                                                 @RequestParam(required = false) String town,
                                                                                 @RequestParam(required = false) String village,
                                                                                 @RequestParam(defaultValue = "1") int page,
                                                                                 @RequestParam(defaultValue = "20") int size) {
        return CommonResponse.ok(service.page(year, county, town, village, page, size));
    }
    /** 更新：按 year/countyDistrict/town/village 定位记录 */
    @PostMapping("/update")
    public CommonResponse<String> update(@RequestBody SanmingTobaccoProductionSituation dto) {
        service.update(dto);
        return CommonResponse.ok("更新成功");
    }
    @PostMapping("/add")
    public CommonResponse<String> add(@RequestBody SanmingTobaccoProductionSituation dto) {
        service.add(dto);
        return CommonResponse.ok("新增成功");
    }

    @DeleteMapping("/delete")
    public CommonResponse<String> delete(@RequestParam Integer year,
                                         @RequestParam String countyDistrict,
                                         @RequestParam String town,
                                         @RequestParam String village) {
        service.delete(year, countyDistrict, town, village);
        return CommonResponse.ok("删除成功");
    }

}
