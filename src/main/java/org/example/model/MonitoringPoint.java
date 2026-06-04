package org.example.model;

import org.example.model.enums.TobaccoFieldType;

import java.time.LocalDateTime;
import java.util.Arrays;

import java.util.List;

public class MonitoringPoint
{
    private int monitoringPointID;
    private double[] coordinates; // 存储坐标数组
    private double elevation; // 海拔
    private TobaccoFieldType tobaccoFieldType; // 枚举类型，表示田地类型
    private String countyDistrict; // 县区
    private String town; // 镇
    private String village; // 村
    private boolean delete; // 删除标识
    private String[] videoCode;// 记录视频地址
    private boolean ifLongTermMonitoringPoint;
    private LocalDateTime samplingDate;
    private String soilSamplingTime;
    private List<String> imagePaths;
    private LocalDateTime deletedAt;

    // 无参构造方法
    public MonitoringPoint() {
    }

    // 带参数的构造方法
    public MonitoringPoint(int monitoringPointID, double[] coordinates, double elevation, TobaccoFieldType tobaccoFieldType,
                           String countyDistrict, String town, String village, boolean delete, String[] videoCode,boolean ifLongTermMonitoringPoint,
                           LocalDateTime samplingDate,String soilSamplingTime,List<String> imagePaths,LocalDateTime deletedAt) {
        this.monitoringPointID = monitoringPointID;
        this.coordinates = coordinates;
        this.elevation = elevation;
        this.tobaccoFieldType = tobaccoFieldType;
        this.countyDistrict = countyDistrict;
        this.town = town;
        this.village = village;
        this.delete = delete;
        this.videoCode = videoCode;
        this.ifLongTermMonitoringPoint = ifLongTermMonitoringPoint;
        this.samplingDate = samplingDate;
        this.soilSamplingTime = soilSamplingTime;
        this.imagePaths = imagePaths;
        this.deletedAt = deletedAt;
    }

    // Getters 和 Setters
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

    public TobaccoFieldType getTobaccoFieldType() {
        return tobaccoFieldType;
    }

    public void setTobaccoFieldType(TobaccoFieldType tobaccoFieldType) {
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

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }
    public String[] getVideoCode() {
        return videoCode;
    }

    public void setVideoCode(String[] videoCode) {
        this.videoCode = videoCode;
    }

    public boolean isIfLongTermMonitoringPoint() {
        return ifLongTermMonitoringPoint;
    }

    public void setIfLongTermMonitoringPoint(boolean ifLongTermMonitoringPoint) {
        this.ifLongTermMonitoringPoint = ifLongTermMonitoringPoint;
    }

    public LocalDateTime getSamplingDate() {
        return samplingDate;
    }
    public void setSamplingDate(LocalDateTime samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getSoilSamplingTime() {
        return soilSamplingTime;
    }
    public void setSoilSamplingTime(String soilSamplingTime) {
        this.soilSamplingTime = soilSamplingTime;
    }

    public List<String> getImagePaths() { return imagePaths; }
    public void setImagePaths(List<String> imagePaths) { this.imagePaths = imagePaths; }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }
    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "MonitoringPoint{" +
                "monitoringPointID=" + monitoringPointID +
                ", coordinates=" + Arrays.toString(coordinates) +
                ", elevation=" + elevation +
                ", tobaccoFieldType=" + tobaccoFieldType +
                ", countyDistrict='" + countyDistrict + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", delete=" + delete +
                ", videoCode=" + Arrays.toString(videoCode) +
                ", ifLongTermMonitoringPoint=" + ifLongTermMonitoringPoint +
                ", samplingDate=" + samplingDate +
                ", soilSamplingTime='" + soilSamplingTime + '\'' +
                ", imagePaths=" + imagePaths +
                '}';
    }
}
