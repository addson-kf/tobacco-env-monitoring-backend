package org.example.model;

public class SanmingTobaccoProductionSituation {
    private String countyDistrict;               //县区
    private String town;                         //乡
    private String village;                      // 村

    private Double area;                         //面积单位亩
    private Double contractQuantity;             //合同量
    private Double acquisitionVolume;            //收购量

    private Integer tobaccoFarming;              //烟农
    private Integer tobaccoTechnic;              //烟技员
    private Integer roastingRooms;               //烤房
    private Integer seedlingShelter;             //苗棚

    private String remarks;                      //备注
    private Integer year;                        //年份

    //无参构造
    public SanmingTobaccoProductionSituation() {

    }
    //有参
    public SanmingTobaccoProductionSituation(String countyDistrict, String town, String village, Double area,
        Double contractQuantity, Double acquisitionVolume, Integer tobaccoFarming, Integer tobaccoTechnic,
        Integer roastingRooms, Integer seedlingShelter, String remarks, Integer year) {
        this.countyDistrict = countyDistrict;
        this.town = town;
        this.village = village;
        this.area = area;
        this.contractQuantity = contractQuantity;
        this.acquisitionVolume = acquisitionVolume;
        this.tobaccoFarming = tobaccoFarming;
        this.tobaccoTechnic = tobaccoTechnic;
        this.roastingRooms = roastingRooms;
        this.seedlingShelter = seedlingShelter;
        this.remarks = remarks;
        this.year = year;
    }
    //getset
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
    public Double getArea() {
        return area;
    }
    public void setArea(Double area) {
        this.area = area;
    }
    public Double getContractQuantity() {
        return contractQuantity;
    }
    public void setContractQuantity(Double contractQuantity) {
        this.contractQuantity = contractQuantity;
    }
    public Double getAcquisitionVolume() {
        return acquisitionVolume;
    }
    public void setAcquisitionVolume(Double acquisitionVolume) {
        this.acquisitionVolume = acquisitionVolume;
    }
    public Integer getTobaccoFarming() {
        return tobaccoFarming;
    }
    public void setTobaccoFarming(Integer tobaccoFarming) {
        this.tobaccoFarming = tobaccoFarming;
    }
    public Integer getTobaccoTechnic() {
        return tobaccoTechnic;
    }
    public void setTobaccoTechnic(Integer tobaccoTechnic) {
        this.tobaccoTechnic = tobaccoTechnic;
    }
    public Integer getRoastingRooms() {
        return roastingRooms;
    }
    public void setRoastingRooms(Integer roastingRooms) {
        this.roastingRooms = roastingRooms;
    }
    public Integer getSeedlingShelter() {
        return seedlingShelter;
    }
    public void setSeedlingShelter(Integer seedlingShelter) {
        this.seedlingShelter = seedlingShelter;
    }
    public String getRemarks() {
        return remarks;
    }
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }

    //tostring
    @Override
    public String toString() {
        return "SanmingTobaccoProductionSituation{" +
                "countyDistrict='" + countyDistrict + '\'' +
                ", town='" + town + '\'' +
                ", village='" + village + '\'' +
                ", area=" + area +
                ", contractQuantity=" + contractQuantity +
                ", acquisitionVolume=" + acquisitionVolume +
                ", tobaccoFarming=" + tobaccoFarming +
                ", tobaccoTechnic=" + tobaccoTechnic +
                ", roastingRooms=" + roastingRooms +
                ", seedlingShelter=" + seedlingShelter +
                ", remarks='" + remarks + '\'' +
                ", year=" + year +
                '}';
    }
}



