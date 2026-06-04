package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.MonitoringPointWithSoilData;

import java.util.List;

@Mapper
public interface MonitoringPointWithSoilDataMapper
{
    List<MonitoringPointWithSoilData> getMonitoringPointWithSoilData();
}
