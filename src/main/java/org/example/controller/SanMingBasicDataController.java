package org.example.controller;

import org.example.dto.SanMingBasicDataListDTO.SanMingBasicDataDTO;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SanMingBasicData.SanMingBasicDataListResponse;
import org.example.service.ISanMingBasicDataService;
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

/**
 * 三明基础数据控制器，处理烟草种植基础数据相关请求。
 */
@RestController
@RequestMapping("#{@apiConfig.sanMingBasicData.basePath}") // 动态注入基础路径
@CrossOrigin(origins = "*")
public class SanMingBasicDataController {

    @Autowired
    private ISanMingBasicDataService sanMingBasicDataService;

    // ========== 基本操作 ==========
    /**
     * 插入新三明基础数据
     */
    @PostMapping("#{@apiConfig.sanMingBasicData.endpoints.insert}")
    public InsertResponse insertSanMingBasicData(@RequestBody SanMingBasicDataDTO dto) {
        return sanMingBasicDataService.insertSanMingBasicData(dto);
    }

    /**
     * 获取未删除的三明基础数据
     */
    @GetMapping("#{@apiConfig.sanMingBasicData.endpoints.active}")
    public SanMingBasicDataListResponse getAllActiveSanMingBasicData() {
        return sanMingBasicDataService.getAllActiveSanMingBasicData();
    }

    /**
     * 获取所有三明基础数据（含已删除）
     */
    @GetMapping("#{@apiConfig.sanMingBasicData.endpoints.all}")
    public SanMingBasicDataListResponse getAllSanMingBasicData() {
        return sanMingBasicDataService.getAllSanMingBasicData();
    }

    // ========== 单条数据操作 ==========
    /**
     * 软删除三明基础数据
     */
    @DeleteMapping("#{@apiConfig.sanMingBasicData.endpoints.delete}")
    public DeleteResponse deleteSanMingBasicDataById(@PathVariable int id) {
        return sanMingBasicDataService.deleteSanMingBasicDataById(id);
    }

    /**
     * 恢复已删除的三明基础数据
     */
    @PostMapping("#{@apiConfig.sanMingBasicData.endpoints.restore}")
    public UpdateResponse restoreSanMingBasicDataById(@PathVariable int id) {
        return sanMingBasicDataService.restoreSanMingBasicDataById(id);
    }

    /**
     * 更新三明基础数据
     */
    @PostMapping("#{@apiConfig.sanMingBasicData.endpoints.update}")
    public UpdateResponse updateSanMingBasicDataById(@RequestBody SanMingBasicDataDTO dto) {
        return sanMingBasicDataService.updateSanMingBasicDataById(dto);
    }

    /**
     * 根据监测点ID查询三明基础数据
     */
    @GetMapping("#{@apiConfig.sanMingBasicData.endpoints.byMonitoringPointId}")
    public SanMingBasicDataListResponse getByMonitoringPointId(@PathVariable int monitoringPointId) {
        return sanMingBasicDataService.getSanMingBasicDataByMonitoringPointId(monitoringPointId);
    }


    //    后端功能增加：
    //    由于前端根据县区显示监测点，后端所有表格需要增加根据县区获取数据的业务层和控制层办法/根据县区获取某一字段的所有数据信息

    /** 按县区分页 */
    @GetMapping("/by-county")
    public PageResult<SanMingBasicDataDTO> pageByCounty(
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pr =  new PageRequest();
        pr.setPage(page);
        pr.setSize(size);
        return sanMingBasicDataService.pageByCounty(pr, countyDistrict);
    }

    /** 按县区获取指定字段所有取值（白名单） */
    @GetMapping("/field-describes")
    public List<Object> listFieldValues(@RequestParam(required = false) String county,
                                        @RequestParam ISanMingBasicDataService.SanMingBasicDataField field) {
        return sanMingBasicDataService.listFieldValuesByCounty(county, field);
    }

    /** 按县/乡/村获取指定字段所有取值（字段白名单） */
    @GetMapping("/field-describes/by-region")
    public List<Object> listFieldValuesByRegion(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam ISanMingBasicDataService.SanMingBasicDataField field
    ) {
        return sanMingBasicDataService.listFieldValuesByRegion(county, town, village, field);
    }


}