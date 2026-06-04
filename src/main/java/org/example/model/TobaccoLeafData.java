package org.example.model;

import org.example.model.enums.SoilQuality;
import org.example.model.enums.TobaccoLeafDataEnum;

import java.time.LocalDateTime;

public class TobaccoLeafData
{
    private int tobaccoDataId; // TobaccoDataId 主键 递增
    private int monitoringPointID; // MonitoringPointID 外键
    private TobaccoLeafDataEnum tobaccoLeafData; // TobaccoLeafData 枚举
    private double value; // Value double
    private LocalDateTime samplingDate; // SamplingDate
    private boolean delete; // isDelete
    private SoilQuality classification; // Classification SoilQuality 枚举
    private String samplingTime;
    private LocalDateTime deletedAt;


    // 无参构造函数
    public TobaccoLeafData() {
    }

    // 带参构造函数
    public TobaccoLeafData(int tobaccoDataId, int monitoringPointID, TobaccoLeafDataEnum tobaccoLeafData,
                           double value, LocalDateTime samplingDate, boolean isDelete, SoilQuality classification, String samplingTime, LocalDateTime deletedAt) {
        this.tobaccoDataId = tobaccoDataId;
        this.monitoringPointID = monitoringPointID;
        this.tobaccoLeafData = tobaccoLeafData;
        this.value = value;
        this.samplingDate = samplingDate;
        this.delete = isDelete;
        this.classification = classification;
        this.samplingTime = samplingTime;
        this.deletedAt = deletedAt;
    }

    // Getter 和 Setter 方法
    public int getTobaccoDataId() {
        return tobaccoDataId;
    }

    public void setTobaccoDataId(int tobaccoDataId) {
        this.tobaccoDataId = tobaccoDataId;
    }

    public int getMonitoringPointID() {
        return monitoringPointID;
    }

    public void setMonitoringPointID(int monitoringPointID) {
        this.monitoringPointID = monitoringPointID;
    }

    public TobaccoLeafDataEnum getTobaccoLeafData() {
        return tobaccoLeafData;
    }

    public void setTobaccoLeafData(TobaccoLeafDataEnum tobaccoLeafData) {
        this.tobaccoLeafData = tobaccoLeafData;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
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

    public void setDelete(boolean isDelete) {
        this.delete = isDelete;
    }

    public SoilQuality getClassification() {
        return classification;
    }

    public void setClassification(SoilQuality classification) {
        this.classification = classification;
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
        return "TobaccoLeafDatas{" +
                "tobaccoDataId=" + tobaccoDataId +
                ", monitoringPointID=" + monitoringPointID +
                ", tobaccoLeafData=" + tobaccoLeafData +
                ", value=" + value +
                ", samplingDate=" + samplingDate +
                ", isDelete=" + delete +
                ", classification=" + classification +
                ", samplingTime='" + samplingTime + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
