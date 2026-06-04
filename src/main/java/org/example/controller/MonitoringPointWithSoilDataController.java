package org.example.controller;
import org.example.response.common.MonitoringPointWithSoilData.MonitoringPointWithSoilDataListResponse;
import org.example.service.IMonitoringPointWithSoilDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 监测点与土壤数据控制器，处理与监测点和土壤数据相关的 HTTP 请求。
 */
@RestController
@RequestMapping("/monitoring-point-with-soil-data")
@CrossOrigin(origins = "*")// 暂时允许所有域名访问
public class MonitoringPointWithSoilDataController
{
    @Autowired
    private IMonitoringPointWithSoilDataService monitoringPointWithSoilDataService;

    /**
     * 获取所有监测点及其土壤数据的接口。
     * 客户端可以通过访问该接口获取监测点与土壤数据的列表。
     *
     * @return 包含监测点与土壤数据列表的响应对象
     */
    @GetMapping
    public MonitoringPointWithSoilDataListResponse getAllMonitoringPointWithSoilData() {
        return monitoringPointWithSoilDataService.getAllMonitoringPointWithSoilData();
    }
}
