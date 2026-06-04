package org.example.controller;


import org.example.config.ApiConfig;
import org.example.dto.MonitoringPointListDTO.MonitoringPointDTO;
import org.example.model.MonitoringPoint;
import org.example.model.enums.SoilAttribute;
import org.example.model.enums.TobaccoLeafDataEnum;
import org.example.model.enums.WeatherType;
import org.example.response.base.DeleteResponse;
import org.example.response.base.InsertResponse;
import org.example.response.base.UpdateResponse;
import org.example.response.common.MonitoringPoint.MonitoringPointListResponse;
import org.example.service.IMonitoringPointService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.example.response.common.CommonResponse;
import org.example.response.common.Page.PageRequest;
import org.example.response.common.Page.PageResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import lombok.RequiredArgsConstructor;

/**
 * 监测点控制器，处理与监测点相关的请求。
 */
@RestController
@RequestMapping("/api/monitoring-points")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class MonitoringPointController {

    @Autowired
    private IMonitoringPointService monitoringPointService;

    @Autowired
    private ApiConfig apiConfig;

    /**
     * 插入新监测点
     */
    @PostMapping("#{@apiConfig.monitoringPoint.endpoints.insert}")
    public InsertResponse insertMonitoringPoint(@RequestBody MonitoringPointDTO monitoringPointDTO) {
        return monitoringPointService.insertMonitoringPoint(monitoringPointDTO);
    }

    /**
     * 逻辑删除监测点
     */
    @DeleteMapping("/{monitoringPointID}")
    public DeleteResponse deleteMonitoringPointByIdFixed(@PathVariable int monitoringPointID) {
        return monitoringPointService.deleteMonitoringPointById(monitoringPointID);
    }

    /**
     * 修改监测点
     */
    @PostMapping("#{@apiConfig.monitoringPoint.endpoints.update}")
    public UpdateResponse updateMonitoringPointById(@RequestBody MonitoringPointDTO monitoringPointDTO) {
        return monitoringPointService.updateMonitoringPointById(monitoringPointDTO);
    }

    /**
     * 恢复监测点
     */
    @PostMapping("/{monitoringPointID}/restore")
    public UpdateResponse restoreMonitoringPointByIdFixed(@PathVariable int monitoringPointID) {
        return monitoringPointService.restoreMonitoringPointById(monitoringPointID);
    }

    /**
     * 查询所有未删除监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.allActive}")
    public MonitoringPointListResponse getAllMonitoringPoints() {
        return monitoringPointService.getAllMonitoringPoints();
    }

    /**
     * 查询所有监测点（含已删除）
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.all}")
    public MonitoringPointListResponse getAllMonitoringPointsWithDeleted() {
        return monitoringPointService.getAllMonitoringPointsWithDeleted();
    }

    /**
     * 根据土壤属性和值范围查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.bySoilCondition}")
    public MonitoringPointListResponse getMonitoringPointsBySoilCondition(
            @RequestParam("soilAttribute") SoilAttribute soilAttribute,
            @RequestParam("minValue") Double minValue,
            @RequestParam("maxValue") Double maxValue
    ) {
        return monitoringPointService.getMonitoringPointsBySoilCondition(soilAttribute, minValue, maxValue);
    }

    /**
     * 根据天气条件查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.byWeatherCondition}")
    public MonitoringPointListResponse getMonitoringPointsByWeatherCondition(
            @RequestParam("weatherType") WeatherType weatherType,
            @RequestParam("minValue") Double minValue,
            @RequestParam("maxValue") Double maxValue
    ) {
        return monitoringPointService.getMonitoringPointsByWeatherCondition(weatherType, minValue, maxValue);
    }

    /**
     * 根据行政区划（县乡村）查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.byAdministrativeDivision}")
    public MonitoringPointListResponse getMonitoringPointsByAdministrativeDivision(
            @RequestParam(required = false) String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village
    ) {
        return monitoringPointService.getMonitoringPointsByAdministrativeDivision(countyDistrict, town, village);
    }

    /**
     * 根据烟叶数据条件查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.byTobaccoCondition}")
    public MonitoringPointListResponse getMonitoringPointsByTobaccoCondition(
            @RequestParam("tobaccoLeafData") TobaccoLeafDataEnum tobaccoLeafData,
            @RequestParam("minValue") Double minValue,
            @RequestParam("maxValue") Double maxValue
    ) {
        return monitoringPointService.getMonitoringPointsByTobaccoCondition(tobaccoLeafData, minValue, maxValue);
    }

    /**
     * 根据种植面积查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.byPlantingArea}")
    public MonitoringPointListResponse getMonitoringPointsByPlantingArea(
            @RequestParam("minValue") Double minValue,
            @RequestParam("maxValue") Double maxValue
    ) {
        return monitoringPointService.getMonitoringPointsByPlantingArea(minValue, maxValue);
    }

    /**
     * 根据计划合同量查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.byPlannedContractAmount}")
    public MonitoringPointListResponse getMonitoringPointsByPlannedContractAmount(
            @RequestParam("minValue") Double minValue,
            @RequestParam("maxValue") Double maxValue
    ) {
        return monitoringPointService.getMonitoringPointsByPlannedContractAmount(minValue, maxValue);
    }

    /**
     * 根据收购数量查询监测点
     */
    @GetMapping("#{@apiConfig.monitoringPoint.endpoints.byPurchaseQuantity}")
    public MonitoringPointListResponse getMonitoringPointsByPurchaseQuantity(
            @RequestParam("minValue") Double minValue,
            @RequestParam("maxValue") Double maxValue
    ) {
        return monitoringPointService.getMonitoringPointsByPurchaseQuantity(minValue, maxValue);
    }



    /**
     * 新分页接口：县/乡/村均可空
     * 例：/api/monitoring-points/page?page=1&size=10&county=三元区&town=某某乡&village=某某村
     */
    @GetMapping("/page")
    public IMonitoringPointService.RegionPage<MonitoringPoint> page(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false, name = "county")  String countyDistrict,
            @RequestParam(required = false) String town,
            @RequestParam(required = false) String village
    ) {
        return monitoringPointService.pageActiveByRegion(countyDistrict, town, village, page, size);
    }



}