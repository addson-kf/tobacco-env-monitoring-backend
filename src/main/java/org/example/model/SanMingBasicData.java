package org.example.model;

import java.time.LocalDateTime;

public class SanMingBasicData {
    private int id;  // 主键，自增
    private int monitoringPointId;  // 监测点 ID（外键）
    private String plantingVariety;  // 种植品种
    private double plantingArea;  // 种植面积
    private double plannedContractAmount;  // 计划合同金额
    private double purchaseQuantity;  // 采购数量
    private String describe;  // 描述
    private boolean delete;  // 是否删除
    private LocalDateTime samplingDate;
    private String samplingTime;

    // 默认构造函数
    public SanMingBasicData() {}

    // 带参数构造函数（删除 administrativeDivision，新增 monitoringPointId）
    public SanMingBasicData(int monitoringPointId, String plantingVariety, double plantingArea,
                            double plannedContractAmount, double purchaseQuantity, String describe, boolean delete,
                            LocalDateTime samplingDate,String samplingTime) {
        this.monitoringPointId = monitoringPointId;
        this.plantingVariety = plantingVariety;
        this.plantingArea = plantingArea;
        this.plannedContractAmount = plannedContractAmount;
        this.purchaseQuantity = purchaseQuantity;
        this.describe = describe;
        this.delete = delete;
        this.samplingDate = samplingDate;
        this.samplingTime = samplingTime;
    }

    // Getter 和 Setter 方法（删除 administrativeDivision 相关，新增 monitoringPointId）
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonitoringPointId() {
        return monitoringPointId;
    }

    public void setMonitoringPointId(int monitoringPointId) {
        this.monitoringPointId = monitoringPointId;
    }

    public String getPlantingVariety() {
        return plantingVariety;
    }

    public void setPlantingVariety(String plantingVariety) {
        this.plantingVariety = plantingVariety;
    }

    public double getPlantingArea() {
        return plantingArea;
    }

    public void setPlantingArea(double plantingArea) {
        this.plantingArea = plantingArea;
    }

    public double getPlannedContractAmount() {
        return plannedContractAmount;
    }

    public void setPlannedContractAmount(double plannedContractAmount) {
        this.plannedContractAmount = plannedContractAmount;
    }

    public double getPurchaseQuantity() {
        return purchaseQuantity;
    }

    public void setPurchaseQuantity(double purchaseQuantity) {
        this.purchaseQuantity = purchaseQuantity;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public boolean getDelete() {
        return delete;
    }

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public LocalDateTime getSamplingDate() {
        return samplingDate;
    }
    public void setSamplingDate(LocalDateTime samplingDate) {
        this.samplingDate = samplingDate;
    }

    public String getSamplingTime() {
        return samplingTime;
    }
    public void setSamplingTime(String samplingTime) {
        this.samplingTime = samplingTime;
    }


    // toString 方法（删除 administrativeDivision，新增 monitoringPointId）
    @Override
    public String toString() {
        return "SanMingBasicData{" +
                "id=" + id +
                ", monitoringPointId=" + monitoringPointId +
                ", plantingVariety='" + plantingVariety + '\'' +
                ", plantingArea=" + plantingArea +
                ", plannedContractAmount=" + plannedContractAmount +
                ", purchaseQuantity=" + purchaseQuantity +
                ", describe='" + describe + '\'' +
                ", isDelete=" + delete +
                ", samplingDate=" + samplingDate +
                ", samplingTime='" + samplingTime + '\'' +
                '}';
    }
}