package org.example.model;
import org.example.model.enums.TobaccoSmokingEvaluation;

import java.time.LocalDateTime;
public class SmokingEvaluationData
{
    // 吸烟评估数据ID（主键）
    private int smokingEvaluationId;

    // 监测点ID（外键）
    private int monitoringPointID;

    // 吸烟评估数据（枚举类型）
    private TobaccoSmokingEvaluation smokingEvaluationData;

    // 描述
    private String describe;

    // 采样时间
    private LocalDateTime samplingDate;

    // 是否已删除（软删除）
    private boolean delete;

    private String soilSamplingTime;

    private LocalDateTime deletedAt;


    // 无参构造函数
    public SmokingEvaluationData() {
    }

    // 有参构造函数
    public SmokingEvaluationData(int smokingEvaluationId, int monitoringPointID, TobaccoSmokingEvaluation smokingEvaluationData,
                                 String describe, LocalDateTime samplingDate, boolean delete, String soilSamplingTime, LocalDateTime deletedAt) {
        this.smokingEvaluationId = smokingEvaluationId;
        this.monitoringPointID = monitoringPointID;
        this.smokingEvaluationData = smokingEvaluationData;
        this.describe = describe;
        this.samplingDate = samplingDate;
        this.delete = delete;
        this.soilSamplingTime = soilSamplingTime;
        this.deletedAt = deletedAt;
    }

    // Getter 和 Setter 方法

    public int getSmokingEvaluationId() {
        return smokingEvaluationId;
    }

    public void setSmokingEvaluationId(int smokingEvaluationId) {
        this.smokingEvaluationId = smokingEvaluationId;
    }

    public int getMonitoringPointId() {
        return monitoringPointID;
    }

    public void setMonitoringPointId(int monitoringPointID) {
        this.monitoringPointID = monitoringPointID;
    }

    public TobaccoSmokingEvaluation getSmokingEvaluationData() {
        return smokingEvaluationData;
    }

    public void setSmokingEvaluationData(TobaccoSmokingEvaluation smokingEvaluationData) {
        this.smokingEvaluationData = smokingEvaluationData;
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

    public String getSoilSamplingTime() {
        return soilSamplingTime;
    }
    public void setSoilSamplingTime(String soilSamplingTime) {
        this.soilSamplingTime = soilSamplingTime;
    }

    public LocalDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    @Override
    public String toString() {
        return "SmokingEvaluationData{" +
                "smokingEvaluationId=" + smokingEvaluationId +
                ", monitoringPointID=" + monitoringPointID +
                ", smokingEvaluationData=" + smokingEvaluationData +
                ", describe='" + describe + '\'' +
                ", samplingDate=" + samplingDate +
                ", delete=" + delete +
                ", samplingTime='" + soilSamplingTime + '\'' +
                ", deletedAt=" + deletedAt +
                '}';
    }
}
