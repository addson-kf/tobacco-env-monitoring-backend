package org.example.model;

public class CountyProgress {
    private Long countyId;              // 自增主键
    private String countyDistrict;      // 县区
    private String town;                // 镇
    private String village;             // 村
    private Float progress;             // 进度 0~100
    private Integer monitoringPointID;  // 关联监测点，可为 null

    // 无参构造函数
    public CountyProgress() {
    }

    // 全参构造函数
    public CountyProgress(Long countyId, String countyDistrict, String town, String village, Float progress, Integer monitoringPointID) {
        this.countyId = countyId;
        this.countyDistrict = countyDistrict;
        this.town = town;
        this.village = village;
        this.progress = progress;
        this.monitoringPointID = monitoringPointID;
    }
    public Long getCountyId() {
        return countyId;
    }
    public void setCountyId(Long countyId) {
        this.countyId = countyId;
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
    public Float getProgress() {
        return progress;
    }
    public void setProgress(Float progress) {
        this.progress = progress;
    }
    public Integer getMonitoringPointID() {
        return monitoringPointID;
    }
    public void setMonitoringPointID(Integer monitoringPointID) {
        this.monitoringPointID = monitoringPointID;
    }

    //tostring方法
    @Override
    public String toString()  {
        return "CountyProgress{" +
                "countyId=" + countyId +
                ", countyDistrict='" + countyDistrict + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", progress=" + progress +
                ", monitoringPointID=" + monitoringPointID +
                '}';
    }
}
