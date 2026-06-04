package org.example.controller;

import org.example.config.ApiConfig;
import org.example.dto.SoilDataListDTO.SoilDataDTO;
import org.example.dto.TobaccoLeafDataListDTO.TobaccoLeafDataDTO;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.TobaccoLeafData.TobaccoLeafDataListResponse;
import org.example.service.ISoilDataService;
import org.example.service.ITobaccoLeafDataService;
import org.example.service.impl.TobaccoLeafDataServiceImpl;
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
@RequestMapping("${api.tobacco-leaf-data.base-path:/api/tobaccoLeafData}")
@CrossOrigin(origins = "*")
public class TobaccoLeafDataController {

    @Autowired
    private ITobaccoLeafDataService tobaccoLeafDataService;

    @Autowired
    private ApiConfig apiConfig;

    /**
     * 插入烟草叶片数据
     */
    @PostMapping("${api.tobacco-leaf-data.endpoints.insert:/insert}")
    public InsertResponse insertTobaccoLeafData(@RequestBody TobaccoLeafDataDTO tobaccoLeafDataDTO) {
        return tobaccoLeafDataService.insertTobaccoLeafData(tobaccoLeafDataDTO);
    }

    /**
     * 获取未删除的烟草叶片数据
     */
    @GetMapping("${api.tobacco-leaf-data.endpoints.all-active:/getAllActive}")
    public TobaccoLeafDataListResponse getAllActiveTobaccoLeafData() {
        return tobaccoLeafDataService.getAllActiveTobaccoLeafData();
    }

    /**
     * 获取所有烟草叶片数据（含已删除）
     */
    @GetMapping("${api.tobacco-leaf-data.endpoints.all:/getAll}")
    public TobaccoLeafDataListResponse getAllTobaccoLeafData() {
        return tobaccoLeafDataService.getAllTobaccoLeafData();
    }

    /**
     * 软删除烟草叶片数据
     */
    @DeleteMapping("${api.tobacco-leaf-data.endpoints.delete:/delete/{tobaccoDataId}}")
    public DeleteResponse deleteTobaccoLeafDataByTobaccoDataId(@PathVariable int tobaccoDataId) {
        return tobaccoLeafDataService.deleteTobaccoLeafDataByTobaccoDataId(tobaccoDataId);
    }

    /**
     * 恢复烟草叶片数据
     */
    @PostMapping("${api.tobacco-leaf-data.endpoints.restore:/restore/{tobaccoDataId}}")
    public UpdateResponse restoreTobaccoLeafDataByTobaccoDataId(@PathVariable int tobaccoDataId) {
        return tobaccoLeafDataService.restoreTobaccoLeafDataByTobaccoDataId(tobaccoDataId);
    }

    /**
     * 按监测点ID查询烟草叶片数据
     */
    @GetMapping("${api.tobacco-leaf-data.endpoints.monitoring-point:/getByMonitoringPointId/{monitoringPointId}}")
    public TobaccoLeafDataListResponse getTobaccoLeafDataByMonitoringPointId(@PathVariable int monitoringPointId) {
        return tobaccoLeafDataService.getTobaccoLeafDataByMonitoringPointId(monitoringPointId);
    }

    /**
     * 更新烟草叶片数据
     */
    @PutMapping("${api.tobacco-leaf-data.endpoints.update:/update}")
    public UpdateResponse updateTobaccoLeafDataByTobaccoDataId(@RequestBody TobaccoLeafDataDTO tobaccoLeafData) {
        return tobaccoLeafDataService.updateTobaccoLeafDataByTobaccoDataId(tobaccoLeafData);
    }


    //    后端功能增加：
    //    由于前端根据县区显示监测点，后端所有表格需要增加根据县区获取数据的业务层和控制层办法/根据县区获取某一字段的所有数据信息

    /** 按县区分页 */
    @GetMapping("/by-county")
    public PageResult<TobaccoLeafDataDTO> pageByCounty(
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        PageRequest pr =  new PageRequest();
        pr.setPage(page);
        pr.setSize(size);
        return tobaccoLeafDataService.pageByCounty(pr, countyDistrict);
    }

    /** 按县区获取指定字段所有取值（白名单） */
    @GetMapping("/field-values")
    public List<Object> listFieldValues(@RequestParam(required = false) String county,
                                        @RequestParam ITobaccoLeafDataService. TobaccoLeafDataField field) {
        return tobaccoLeafDataService.listFieldValuesByCounty(county, field);
    }



}