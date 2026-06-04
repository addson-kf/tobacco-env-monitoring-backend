package org.example.service.impl;

import org.example.dao.IMonitoringPointWithSoilDataDao;
import org.example.dto.MonitoringPointWithSoilDataListDTO.MonitoringPointWithSoilDataDTO;
import org.example.dto.MonitoringPointWithSoilDataListDTO.MonitoringPointWithSoilDataListDTO;
import org.example.model.MonitoringPointWithSoilData;
import org.example.response.common.MonitoringPointWithSoilData.MonitoringPointWithSoilDataListResponse;
import org.example.service.IMonitoringPointWithSoilDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class MonitoringPointWithSoilDataServiceImpl implements IMonitoringPointWithSoilDataService
{
    @Autowired
    private final IMonitoringPointWithSoilDataDao monitoringPointWithSoilDataDao;

    @Autowired
    public MonitoringPointWithSoilDataServiceImpl(IMonitoringPointWithSoilDataDao monitoringPointWithSoilDataDao) {
        this.monitoringPointWithSoilDataDao = monitoringPointWithSoilDataDao;
    }

    /**
     * 获取所有监测点及其土壤数据，并返回响应给客户端。
     *
     * @return 包含监测点与土壤数据列表的响应对象
     */
    @Override
    public MonitoringPointWithSoilDataListResponse getAllMonitoringPointWithSoilData() {
        try {
            List<MonitoringPointWithSoilData> dataList = monitoringPointWithSoilDataDao.getAllMonitoringPointWithSoilData();
            System.out.println("在这里！！！" + dataList.get(1).getSoilDataList().get(1).getSoilSamplingTime());
            List<MonitoringPointWithSoilDataDTO> dtoList = dataList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            MonitoringPointWithSoilDataListDTO listDTO = new MonitoringPointWithSoilDataListDTO(dtoList);
            System.out.println("在这里！！！" + listDTO.getMonitoringPointWithSoilDataList().get(1).getSoilDataList().get(1).getSoilSamplingTime());
            return MonitoringPointWithSoilDataListResponse.ok(listDTO);
        } catch (Exception e) {
            return MonitoringPointWithSoilDataListResponse.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 打印所有监测点与土壤数据的数量和详细信息（用于调试）。
     */
    @Override
    public void printMonitoringPointWithSoilData() {
        MonitoringPointWithSoilDataListResponse response = getAllMonitoringPointWithSoilData();
        if (response.isSuccess()) {
            List<MonitoringPointWithSoilDataDTO> dtoList = response.getData().getMonitoringPointWithSoilDataList();
            for (MonitoringPointWithSoilDataDTO monitoringPointWithSoilDataDTO : dtoList) {
                System.out.println(monitoringPointWithSoilDataDTO. toString());
            }

            System.out.println("总共监测点条目数量：" + dtoList.size());
        } else {
            System.out.println("查询失败: " + response.getMessage());
        }
    }

    /**
     * 将 MonitoringPointWithSoilData 实体对象转换为 MonitoringPointWithSoilDataDTO 传输对象。
     *
     * @param data 监测点与土壤数据实体对象
     * @return 监测点与土壤数据传输对象
     */
    private MonitoringPointWithSoilDataDTO convertToDTO(MonitoringPointWithSoilData data) {
        MonitoringPointWithSoilDataDTO dto = new MonitoringPointWithSoilDataDTO();
        dto.setIfLongTermMonitoringPoint(data.isIfLongTermMonitoringPoint());
        dto.setMonitoringPointID(data.getMonitoringPointID());
        dto.setCoordinates(data.getCoordinates());
        dto.setElevation(data.getElevation());
        dto.setTobaccoFieldType(data.getTobaccoFieldType());
        dto.setCountyDistrict(data.getCountyDistrict());
        dto.setTown(data.getTown());
        dto.setVillage(data.getVillage());
        dto.setSoilDataList(data.getSoilDataList().stream()
                .map(soilData -> {
                    org.example.dto.SoilDataListDTO.SoilDataDTO soilDataDTO = new org.example.dto.SoilDataListDTO.SoilDataDTO();
                    soilDataDTO.setSoilDataID(soilData.getSoilDataID());
                    soilDataDTO.setMonitoringPointID(soilData.getMonitoringPointID());
                    soilDataDTO.setSoilAttribute(soilData.getSoilAttribute());
                    soilDataDTO.setValue(soilData.getValue());
                    soilDataDTO.setSamplingDate(soilData.getSamplingDate());
                    soilDataDTO.setDeleted(soilData.isDelete());
                    soilDataDTO.setClassification(soilData.getClassification());
                    soilDataDTO.setOvenNumber(soilData.getOvenNumber());
                    soilDataDTO.setNotes(soilData.getNotes());
                    soilDataDTO.setFarmerName(soilData.getFarmerName());
                    soilDataDTO.setSoilSamplingTime(soilData.getSoilSamplingTime());
                    return soilDataDTO;
                })
                .collect(Collectors.toList()));
        return dto;
    }
}
