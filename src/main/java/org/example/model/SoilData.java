package org.example.model;

import org.example.model.enums.SoilAttribute;
import org.example.model.enums.SoilQuality;

import java.time.LocalDateTime;

public class SoilData
{
    private int soilDataID;
    private int monitoringPointID;
    private SoilAttribute soilAttribute;
    private double value;
    private LocalDateTime samplingDate;
    private boolean delete;
    private SoilQuality classification;
    private String ovenNumber;
    private String notes;
    private String farmerName;
    private String soilSamplingTime;

    public String getSoilSamplingTime()
    {
        return soilSamplingTime;
    }

    public void setSoilSamplingTime(String soilSamplingTime)
    {
        this.soilSamplingTime = soilSamplingTime;
    }

    // 无参构造函数
    public SoilData() {
        this.samplingDate = LocalDateTime.now();
        this.soilSamplingTime = "After harvesting rice";
    }

    // 全参构造函数
    public SoilData(int soilDataID, int monitoringPointID, SoilAttribute soilAttribute, double value,
                    LocalDateTime samplingDate, boolean delete, SoilQuality classification,
                    String ovenNumber,
                    String notes, String farmerName,String soilSamplingTime) {
        this.soilDataID = soilDataID;
        this.monitoringPointID = monitoringPointID;
        this.soilAttribute = soilAttribute;
        this.value = value;
        this.samplingDate = samplingDate;
        this.delete = delete;
        this.classification = classification;
        this.ovenNumber = ovenNumber;
        this.notes = notes;
        this.farmerName = farmerName;
        this.soilSamplingTime = soilSamplingTime;
    }

    // Getters 和 Setters

    public int getSoilDataID() {
        return soilDataID;
    }

    public void setSoilDataID(int soilDataID) {
        this.soilDataID = soilDataID;
    }

    public int getMonitoringPointID() {
        return monitoringPointID;
    }

    public void setMonitoringPointID(int monitoringPointID) {
        this.monitoringPointID = monitoringPointID;
    }

    public SoilAttribute getSoilAttribute() {
        return soilAttribute;
    }

    public void setSoilAttribute(SoilAttribute soilAttribute) {
        this.soilAttribute = soilAttribute;
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

    public void setDelete(boolean delete) {
        this.delete = delete;
    }

    public SoilQuality getClassification() {
        return classification;
    }

    public void setClassification(SoilQuality classification) {
        this.classification = classification;
    }

    public String getOvenNumber() {
        return ovenNumber;
    }

    public void setOvenNumber(String ovenNumber) {
        this.ovenNumber = ovenNumber;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    // 输出所有字段用于测试
    @Override
    public String toString() {
        return "SoilData{" +
                "soilDataID=" + soilDataID +
                ", monitoringPointID=" + monitoringPointID +
                ", soilAttribute='" + soilAttribute + '\'' +
                ", value=" + value +
                ", samplingDate=" + samplingDate +
                ", delete=" + delete +
                ", classification='" + classification + '\'' +
                ", ovenNumber='" + ovenNumber + '\'' +
                ", notes='" + notes + '\'' +
                ", farmerName='" + farmerName + '\'' +
                '}';
    }
}
