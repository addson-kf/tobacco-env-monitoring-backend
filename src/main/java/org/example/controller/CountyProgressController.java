package org.example.controller;

import org.example.dto.CountyProgressDTO.CountyProgressDTO;
import org.example.dto.CountyProgressDTO.CountyProgressListDTO;
import org.example.dto.CountyProgressDTO.CountyProgressUpsertRequest;
import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageResult;
import org.example.service.ICountyProgressService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/county-progress")
public class CountyProgressController {
    private final ICountyProgressService service;
    public CountyProgressController(ICountyProgressService service) {
        this.service = service;
    }
    /** 插入或更新（按 县/镇/村 唯一行） */
    @PostMapping("/upsert")
    public CommonResponse<Void> upsert(@RequestBody CountyProgressUpsertRequest req) {
        service.upsert(req);
        return CommonResponse.ok("ok", null);
    }

    /** 根据 CountyId 更新（选择性字段） */
    @PutMapping
    public CommonResponse<Void> update(@RequestBody CountyProgressDTO dto) {
        service.updateById(dto);
        return CommonResponse.ok("ok", null);
    }

    /** 单条查询（县/镇/村） */
    @GetMapping("/one")
    public CommonResponse<CountyProgressDTO> one(@RequestParam String county,
                                                 @RequestParam String town,
                                                 @RequestParam String village) {
        return CommonResponse.ok(service.findOne(county, town, village));
    }

    /** 列表（可过滤） */
    @GetMapping
    public CommonResponse<CountyProgressListDTO> list(
            @RequestParam(required=false) String county,
            @RequestParam(required=false) String town,
            @RequestParam(required=false) String village) {
        List<CountyProgressDTO> list = service.list(county, town, village);
        return CommonResponse.ok(new CountyProgressListDTO(list));
    }

    /** 分页 */
    @GetMapping("/page")
    public CommonResponse<PageResult<CountyProgressDTO>> page(@RequestParam(required=false) String county,
                                                              @RequestParam(required=false) String town,
                                                              @RequestParam(required=false) String village,
                                                              @RequestParam(defaultValue = "1") int page,
                                                              @RequestParam(defaultValue = "20") int size) {
        return CommonResponse.ok(service.page(county, town, village, page, size));
    }

    /**
     * 手动触发进度重算
     *
     * 示例调用：
     * POST /api/county-progress/recompute?county=建宁&town=里心&year=2024&stage=种烟后
     *
     * @param county 县区名称，可选
     * @param town   乡镇名称，可选
     * @param year   年份，必选
     * @param stage  阶段（种烟前 / 种烟后 / 种稻后），可选
     * @return 通用响应
     */
    @PostMapping("/recompute")
    public CommonResponse<Void> recomputeProgress(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) String stage) {
        try {
            service.recomputeProgress(county, town, year, stage);
            return CommonResponse.ok("进度计算完成", null);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.error("进度计算失败：" + e.getMessage());
        }
    }

    /**
     * （可选）查询当前县区的进度
     *
     * 示例：
     * GET /api/county-progress/list?county=建宁&town=里心
     */
    @GetMapping("/list")
    public CommonResponse<?> listProgress(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village) {
        try {
            return CommonResponse.ok(
                    "查询成功",
                    service.list(county, town, village)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return CommonResponse.error("查询失败：" + e.getMessage());
        }
    }
}
