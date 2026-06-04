package org.example.service;
import org.example.model.MonitoringPointWithSoilData;
import org.example.response.common.MonitoringPointWithSoilData.MonitoringPointWithSoilDataListResponse;

import java.util.List;

/**
 * Service 接口：提供监测点与土壤数据的业务处理接口。
 */
public interface IMonitoringPointWithSoilDataService
{
    /**
     * 获取所有监测点及其土壤数据，并返回响应给客户端。
     *
     * @return 包含监测点与土壤数据列表的响应对象
     */
    MonitoringPointWithSoilDataListResponse getAllMonitoringPointWithSoilData();

    /**
     * 打印所有监测点与土壤数据的数量和详细信息（用于调试）。
     */
    void printMonitoringPointWithSoilData();
}
