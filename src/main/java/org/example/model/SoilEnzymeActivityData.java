package org.example.model;

import org.example.model.enums.SoilEnzymeActivityAnalysis;

import java.time.LocalDateTime;

/**
 * 土壤酶活性数据模型类
 * 表示土壤酶活性数据的记录，包括监测点ID、酶活性数据、值、描述、采样日期等信息。
 */
public class SoilEnzymeActivityData
{
    // 主键，土壤酶活性数据的ID
    private int soilEnzymeActivityId;

    // 外键，监测点ID
    private int monitoringPointId;

    // 土壤酶活性分析类型
    private SoilEnzymeActivityAnalysis soilEnzymeActivityData;

    // 活性值
    private double value;

    // 描述
    private String describe;

    // 采样日期
    private LocalDateTime samplingDate;

    // 数据是否被删除
    private boolean delete;

    private String samplingTime;

    private LocalDateTime deletedAt;


    // 默认构造函数
    public SoilEnzymeActivityData() {
    }

    /**
     * 带有所有字段的构造函数
     *
     * @param soilEnzymeActivityId      土壤酶活性数据的ID
     * @param monitoringPointId         监测点ID
     * @param soilEnzymeActivityData    土壤酶活性分析类型
     * @param value                     酶活性值
     * @param describe                  描述
     * @param samplingDate              采样日期
     * @param delete                    是否删除标记
     */
    public SoilEnzymeActivityData(int soilEnzymeActivityId, int monitoringPointId,
                                  SoilEnzymeActivityAnalysis soilEnzymeActivityData, double value,
                                  String describe, LocalDateTime samplingDate, boolean delete, String samplingTime, LocalDateTime deletedAt) {
        this.soilEnzymeActivityId = soilEnzymeActivityId;
        this.monitoringPointId = monitoringPointId;
        this.soilEnzymeActivityData = soilEnzymeActivityData;
        this.value = value;
        this.describe = describe;
        this.samplingDate = samplingDate;
        this.delete = delete;
        this.samplingTime = samplingTime;
        this.deletedAt = deletedAt;
    }

    // Getter 和 Setter 方法
    public int getSoilEnzymeActivityId() {
        return soilEnzymeActivityId;
    }

    public void setSoilEnzymeActivityId(int soilEnzymeActivityId) {
        this.soilEnzymeActivityId = soilEnzymeActivityId;
    }

    public int getMonitoringPointId() {
        return monitoringPointId;
    }

    public void setMonitoringPointId(int monitoringPointId) {
        this.monitoringPointId = monitoringPointId;
    }

    public SoilEnzymeActivityAnalysis getSoilEnzymeActivityData() {
        return soilEnzymeActivityData;
    }

    public void setSoilEnzymeActivityData(SoilEnzymeActivityAnalysis soilEnzymeActivityData) {
        this.soilEnzymeActivityData = soilEnzymeActivityData;
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

    public LocalDateTime getDeletedAt() { return deletedAt; }
    public void setDeletedAt(LocalDateTime deletedAt) { this.deletedAt = deletedAt; }
    /**
     * 重写toString方法，输出对象的所有属性
     */
    @Override
    public String toString() {
        return "SoilEnzymeActivityData{" +
                "soilEnzymeActivityId=" + soilEnzymeActivityId +
                ", monitoringPointId=" + monitoringPointId +
                ", soilEnzymeActivityData=" + soilEnzymeActivityData +
                ", value=" + value +
                ", describe='" + describe + '\'' +
                ", samplingDate=" + samplingDate +
                ", delete=" + delete +
                ", samplingTime='" + samplingTime + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
