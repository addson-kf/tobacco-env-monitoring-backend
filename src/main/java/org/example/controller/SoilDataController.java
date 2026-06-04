package org.example.controller;

import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.CommonResponse;
import org.example.response.common.SoilData.SoilDataListResponse;
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
 * 土壤数据控制器，处理与土壤数据相关的请求。
 */
@RestController
@RequestMapping("${api.soilData.basePath:/api/soilData}") // 支持默认值
@CrossOrigin(origins = "*")
public class SoilDataController {

    @Autowired
    private ISoilDataService soilDataService;

    /**
     * 查询未删除的土壤数据
     */
    @GetMapping("${api.soilData.endpoints.allActive:/allActive}")
    public SoilDataListResponse findAll() {
        return soilDataService.findAll();
    }

    /**
     * 查询所有土壤数据（含已删除）
     */
    @GetMapping("${api.soilData.endpoints.all:/all}")
    public SoilDataListResponse findAllForAdmin() {
        return soilDataService.findAllForAdmin();
    }

    /**
     * 插入土壤数据
     */
    @PostMapping("${api.soilData.endpoints.insert:/insert}")
    public InsertResponse insertWithClassification(@RequestBody SoilDataDTO soilDataDTO) {
        return soilDataService.insertWithClassification(soilDataDTO);
    }

    /**
     * 软删除土壤数据
     */
    @DeleteMapping("${api.soilData.endpoints.delete:/delete/{soilDataID}}")
    public DeleteResponse softDeleteById(@PathVariable int soilDataID) {
        return soilDataService.softDeleteById(soilDataID);
    }

    /**
     * 恢复土壤数据
     */
    @PostMapping("${api.soilData.endpoints.restore:/restore/{soilDataID}}")
    public UpdateResponse restoreById(@PathVariable int soilDataID) {
        return soilDataService.restoreById(soilDataID);
    }

    /**
     * 更新土壤数据
     */
    @PostMapping("${api.soilData.endpoints.update:/update}")
    public UpdateResponse updateDataWithClassification(@RequestBody SoilDataDTO soilDataDTO) {
        return soilDataService.updateDataWithClassification(soilDataDTO);
    }

    /**
     * 更新土壤分类
     */
    @PostMapping("${api.soilData.endpoints.updateClassification:/updateClassification}")
    public UpdateResponse updateClassificationForSpecificAttribute(@RequestBody Map<String, String> updateInfo) {
        if (!updateInfo.containsKey("soilAttribute")) {
            return new UpdateResponse(false, "土壤属性不能为空");
        }
        String soilAttribute = updateInfo.get("soilAttribute");
        return soilDataService.updateClassificationForSpecificAttribute(soilAttribute);
    }

    /**
     * 查询指定ID的土壤数据详情
     */
    @GetMapping("${api.soilData.endpoints.byId:/byId/{soilDataID}}")
    public SoilDataListResponse findById(@PathVariable int soilDataID) {
        return soilDataService.findById(soilDataID);
    }



    //    后端功能增加：
   //    由于前端根据县区显示监测点，后端所有表格需要增加根据县区获取数据的业务层和控制层办法/根据县区获取某一字段的所有数据信息

    /** 按县区分页 */
    @GetMapping("/by-county")
    public PageResult<SoilDataDTO> pageByCounty(
                                                @RequestParam(required = false) String countyDistrict,
                                                @RequestParam(defaultValue = "1") int page,
                                                @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pr =  new PageRequest();
        pr.setPage(page);
        pr.setSize(size);
        return soilDataService.pageByCounty(pr, countyDistrict);
    }

    /** 按县区获取指定字段所有取值（白名单） */
    @GetMapping("/field-values")
    public List<Object> listFieldValues(@RequestParam(required = false) String county,
                                        @RequestParam ISoilDataService.SoilDataField field) {
        return soilDataService.listFieldValuesByCounty(county, field);
    }


    /** 按县/乡/村获取指定字段所有取值（字段白名单） */
    @GetMapping("/field-values/by-region")
    public List<Object> listFieldValuesByRegion(
            @RequestParam(required = false) String county,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village,
            @RequestParam ISoilDataService.SoilDataField field
    ) {
        return soilDataService.listFieldValuesByRegion(county, town, village, field);
    }

    //上传文件接口
    @PostMapping("/batchInsert")
    public InsertResponse batchInsert(@RequestBody List<SoilDataDTO> soilDataDTOList) {
        return soilDataService.batchInsertWithClassification(soilDataDTOList);
    }


}