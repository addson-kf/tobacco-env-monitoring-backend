package org.example.controller;

import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.SoilEnzymeActivityDataListDTO.SoilEnzymeActivityDataDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.SoilEnzymeActivity.SoilEnzymeActivityDataListResponse;
import org.example.service.ISoilDataService;
import org.example.service.ISoilEnzymeActivityDataService;
import org.example.config.ApiConfig;
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
 * 土壤酶活性数据控制器，处理与土壤酶活性数据相关的 HTTP 请求
 */
@RestController
@RequestMapping("${api.soilEnzymeActivityData.basePath:/api/soilEnzymeActivityData}")
@CrossOrigin(origins = "*")
public class SoilEnzymeActivityDataController {

    @Autowired
    private ISoilEnzymeActivityDataService soilEnzymeActivityDataService;

    @Autowired
    private ApiConfig apiConfig; // 注入配置类

    /**
     * 插入土壤酶活性数据（管理员专属，写操作）
     */
    @PostMapping("${api.soilEnzymeActivityData.endpoints.insert:/insert}")
    public InsertResponse insertSoilEnzymeActivityData(@RequestBody SoilEnzymeActivityDataDTO soilEnzymeActivityData) {
        return soilEnzymeActivityDataService.insertSoilEnzymeActivityData(soilEnzymeActivityData);
    }

    /**
     * 获取未删除的土壤酶活性数据（普通用户/管理员均可访问）
     */
    @GetMapping("${api.soilEnzymeActivityData.endpoints.allActive:/allActive}")
    public SoilEnzymeActivityDataListResponse getAllActiveSoilEnzymeActivityData() {
        return soilEnzymeActivityDataService.getAllActiveSoilEnzymeActivityData();
    }

    /**
     * 获取所有土壤酶活性数据（含已删除，管理员专属）
     */
    @GetMapping("${api.soilEnzymeActivityData.endpoints.all:/all}")
    public SoilEnzymeActivityDataListResponse getAllSoilEnzymeActivityData() {
        return soilEnzymeActivityDataService.getAllSoilEnzymeActivityData();
    }

    /**
     * 软删除土壤酶活性数据（管理员专属，写操作）
     */
    @DeleteMapping("${api.soilEnzymeActivityData.endpoints.delete:/delete/{soilEnzymeActivityId}}")
    public DeleteResponse deleteSoilEnzymeActivityDataById(@PathVariable int soilEnzymeActivityId) {
        return soilEnzymeActivityDataService.deleteSoilEnzymeActivityDataById(soilEnzymeActivityId);
    }

    /**
     * 恢复土壤酶活性数据（管理员专属，写操作）
     */
    @PostMapping("${api.soilEnzymeActivityData.endpoints.restore:/restore/{soilEnzymeActivityId}}")
    public UpdateResponse restoreSoilEnzymeActivityDataById(@PathVariable int soilEnzymeActivityId) {
        return soilEnzymeActivityDataService.restoreSoilEnzymeActivityDataById(soilEnzymeActivityId);
    }

    /**
     * 按监测点ID查询土壤酶活性数据（普通用户/管理员均可访问）
     */
    @GetMapping("#{@apiConfig.soilEnzymeActivityData.endpoints.ByMonitoringPointId}")
    public SoilEnzymeActivityDataListResponse getSoilEnzymeActivityDataByMonitoringPointId(@PathVariable int monitoringPointId) {
        return soilEnzymeActivityDataService.getSoilEnzymeActivityDataByMonitoringPointId(monitoringPointId);
    }

    /**
     * 更新土壤酶活性数据（管理员专属，写操作）
     */
    @GetMapping("${api.soilEnzymeActivityData.endpoints.ByMonitoringPointId:/by-monitoring-point/{monitoringPointId}}")
    public UpdateResponse updateSoilEnzymeActivityDataById(@RequestBody SoilEnzymeActivityDataDTO soilEnzymeActivityData) {
        return soilEnzymeActivityDataService.updateSoilEnzymeActivityDataById(soilEnzymeActivityData);
    }




    //    后端功能增加：
    //    由于前端根据县区显示监测点，后端所有表格需要增加根据县区获取数据的业务层和控制层办法/根据县区获取某一字段的所有数据信息

    /** 按县区分页 */
    @GetMapping("/by-county")
    public PageResult<SoilEnzymeActivityDataDTO> pageByCounty(
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pr =  new PageRequest();
        pr.setPage(page);
        pr.setSize(size);
        return soilEnzymeActivityDataService.pageByCounty(pr, countyDistrict);
    }

    /** 按县区获取指定字段所有取值（白名单） */
    @GetMapping("/field-values")
    public List<Object> listFieldValues(@RequestParam(required = false) String county,
                                        @RequestParam ISoilEnzymeActivityDataService.ISoilEnzymeActivityDataField field) {
        return soilEnzymeActivityDataService.listFieldValuesByCounty(county, field);
    }

    /** 按县/乡/村获取指定字段所有取值（字段白名单） */
    @GetMapping("/field-values/by-region")
    public List<Object> listFieldValuesByRegion(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam ISoilEnzymeActivityDataService.ISoilEnzymeActivityDataField field
    ) {
        return soilEnzymeActivityDataService.listFieldValuesByRegion(county, town, village, field);
    }


}