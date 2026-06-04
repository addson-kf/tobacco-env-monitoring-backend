package org.example.model;

import java.util.List;

public class MonitoringPointWithSoilData
{
    private int monitoringPointID;
    private double[] coordinates;
    private double elevation;
    private String tobaccoFieldType;
    private String countyDistrict;
    private String town;
    private String village;
    private boolean ifLongTermMonitoringPoint;

    private List<SoilData> soilDataList;  // 一对多关系：一个监测点多个土壤数据

    // 无参构造器
    public MonitoringPointWithSoilData() {
    }

    // 全参构造器
    public MonitoringPointWithSoilData(int monitoringPointID, double[] coordinates, double elevation,
                                       String tobaccoFieldType, String countyDistrict, String town,
                                       String village,boolean ifLongTermMonitoringPoint, List<SoilData> soilDataList) {
        this.monitoringPointID = monitoringPointID;
        this.coordinates = coordinates;
        this.elevation = elevation;
        this.tobaccoFieldType = tobaccoFieldType;
        this.countyDistrict = countyDistrict;
        this.town = town;
        this.village = village;
        this.ifLongTermMonitoringPoint = ifLongTermMonitoringPoint;
        this.soilDataList = soilDataList;
    }

    // Getters and Setters

    public int getMonitoringPointID() {
        return monitoringPointID;
    }

    public void setMonitoringPointID(int monitoringPointID) {
        this.monitoringPointID = monitoringPointID;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public String getTobaccoFieldType() {
        return tobaccoFieldType;
    }

    public void setTobaccoFieldType(String tobaccoFieldType) {
        this.tobaccoFieldType = tobaccoFieldType;
    }

    public String getCountyDistrict() {
        return countyDistrict;
    }

    public void setCountyDistrict(String countyDistrict) {
        this.countyDistrict = countyDistrict;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public boolean isIfLongTermMonitoringPoint() {
        return ifLongTermMonitoringPoint;
    }

    public void setIfLongTermMonitoringPoint(boolean ifLongTermMonitoringPoint) {
        this.ifLongTermMonitoringPoint = ifLongTermMonitoringPoint;
    }

    public List<SoilData> getSoilDataList() {
        return soilDataList;
    }

    public void setSoilDataList(List<SoilData> soilDataList) {
        this.soilDataList = soilDataList;
    }

    @Override
    public String toString() {
        StringBuilder soilDataStr = new StringBuilder();
        if (soilDataList != null && !soilDataList.isEmpty()) {
            soilDataStr.append("[\n");
            for (SoilData soilData : soilDataList) {
                soilDataStr.append("  ").append(soilData.toString()).append(",\n");
            }
            soilDataStr.append("]");
        } else {
            soilDataStr.append("null");
        }

        return "MonitoringPointWithSoilData{" +
                "monitoringPointID='" + monitoringPointID + '\'' +
                ", coordinates=" + (coordinates != null ? java.util.Arrays.toString(coordinates) : "null") +
                ", elevation=" + elevation +
                ", tobaccoFieldType='" + tobaccoFieldType + '\'' +
                ", countyDistrict='" + countyDistrict + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", ifLongTermMonitoringPoint=" + ifLongTermMonitoringPoint +
                ", soilDataList=" + soilDataStr.toString() +
                '}';
    }
}
