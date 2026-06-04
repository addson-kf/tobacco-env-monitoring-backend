package org.example.model;

import org.example.model.enums.WeatherType;

import java.security.Timestamp;
import java.time.LocalDateTime;

public class WeatherData
{
    private int weatherId;  // 主键，自增
    private int monitoringPointId;  // 外键
    private WeatherType weatherType;  // 枚举类型
    private double value;  // 值
    private String describe;  // 描述
    private String acquisitionCycle;  // 获取周期
    private String dataSource;  // 数据源
    private LocalDateTime samplingDate;  // 采样日期
    private boolean delete;  // 是否删除
    private String samplingTime;
    private LocalDateTime deletedAt;


    // 默认构造函数
    public WeatherData() {}

    // 带参数构造函数
    public WeatherData(int monitoringPointId, WeatherType weatherType, double value,
                       String describe, String acquisitionCycle, String dataSource,
                       LocalDateTime samplingDate, boolean delete, String samplingTime, LocalDateTime deletedAt) {
        this.monitoringPointId = monitoringPointId;
        this.weatherType = weatherType;
        this.value = value;
        this.describe = describe;
        this.acquisitionCycle = acquisitionCycle;
        this.dataSource = dataSource;
        this.samplingDate = samplingDate;
        this.delete = delete;
        this.samplingTime = samplingTime;
        this.deletedAt = deletedAt;
    }

    // Getter 和 Setter 方法
    public int getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(int weatherId) {
        this.weatherId = weatherId;
    }

    public int getMonitoringPointId() {
        return monitoringPointId;
    }

    public void setMonitoringPointId(int monitoringPointId) {
        this.monitoringPointId = monitoringPointId;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(WeatherType weatherType) {
        this.weatherType = weatherType;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getAcquisitionCycle() {
        return acquisitionCycle;
    }

    public void setAcquisitionCycle(String acquisitionCycle) {
        this.acquisitionCycle = acquisitionCycle;
    }

    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public LocalDateTime getSamplingDate() {
        return samplingDate;
    }

    public void setSamplingDate(LocalDateTime samplingDate) {
        this.samplingDate = samplingDate;
    }

    public boolean isDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public String getSamplingTime() {
        return samplingTime;
    }

    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
    // toString 方法
    @Override
    public String toString() {
        return "WeatherData{" +
                "weatherId=" + weatherId +
                ", monitoringPointId=" + monitoringPointId +
                ", weatherType=" + weatherType +
                ", value=" + value +
                ", describe='" + describe + '\'' +
                ", acquisitionCycle='" + acquisitionCycle + '\'' +
                ", dataSource='" + dataSource + '\'' +
                ", samplingDate=" + samplingDate +
                ", delete=" + delete +
                ", samplingTime='" + samplingTime + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
