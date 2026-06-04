package org.example.model;

import org.locationtech.jts.geom.Geometry;
import java.math.BigDecimal;
import java.util.Objects;
public class SanmingxianquPolygon
{
    private Integer gid;                // 主键ID
    private BigDecimal area;            // 面积
    private BigDecimal perimeter;       // 周长
    private Double bount_;              // 边界相关字段
    private Double bountId;             // 边界ID
    private String gbcode;              // 国标编码
    private String adcode99;            // 行政区划编码
    private String name99;              // 名称
    private String sh2;                 // 省代码
    private String di2;                 // 市代码
    private String x2;                  // 县代码
    private BigDecimal centroidY;       // 中心点Y坐标
    private BigDecimal centroidX;       // 中心点X坐标
    private BigDecimal phValue;         // pH值
    private Geometry geom;             // 几何图形数据

    public Integer getGid()
    {
        return gid;
    }

    public void setGid(Integer gid)
    {
        this.gid = gid;
    }

    public BigDecimal getArea()
    {
        return area;
    }

    public void setArea(BigDecimal area)
    {
        this.area = area;
    }

    public BigDecimal getPerimeter()
    {
        return perimeter;
    }

    public void setPerimeter(BigDecimal perimeter)
    {
        this.perimeter = perimeter;
    }

    public Double getBount_()
    {
        return bount_;
    }

    public void setBount_(Double bount_)
    {
        this.bount_ = bount_;
    }

    public Double getBountId()
    {
        return bountId;
    }

    public void setBountId(Double bountId)
    {
        this.bountId = bountId;
    }

    public String getGbcode()
    {
        return gbcode;
    }

    public void setGbcode(String gbcode)
    {
        this.gbcode = gbcode;
    }

    public String getAdcode99()
    {
        return adcode99;
    }

    public void setAdcode99(String adcode99)
    {
        this.adcode99 = adcode99;
    }

    public String getName99()
    {
        return name99;
    }

    public void setName99(String name99)
    {
        this.name99 = name99;
    }

    public String getSh2()
    {
        return sh2;
    }

    public void setSh2(String sh2)
    {
        this.sh2 = sh2;
    }

    public String getDi2()
    {
        return di2;
    }

    public void setDi2(String di2)
    {
        this.di2 = di2;
    }

    public String getX2()
    {
        return x2;
    }

    public void setX2(String x2)
    {
        this.x2 = x2;
    }

    public BigDecimal getCentroidY()
    {
        return centroidY;
    }

    public void setCentroidY(BigDecimal centroidY)
    {
        this.centroidY = centroidY;
    }

    public BigDecimal getCentroidX()
    {
        return centroidX;
    }

    public void setCentroidX(BigDecimal centroidX)
    {
        this.centroidX = centroidX;
    }

    public BigDecimal getPhValue()
    {
        return phValue;
    }

    public void setPhValue(BigDecimal phValue)
    {
        this.phValue = phValue;
    }

    public Geometry getGeom()
    {
        return geom;
    }

    public void setGeom(Geometry geom)
    {
        this.geom = geom;
    }

    // 无参构造方法
    public SanmingxianquPolygon() {
    }

    // 全参构造方法
    public SanmingxianquPolygon(Integer gid, BigDecimal area, BigDecimal perimeter, Double bount_,
                                Double bountId, String gbcode, String adcode99, String name99,
                                String sh2, String di2, String x2, BigDecimal centroidY,
                                BigDecimal centroidX, BigDecimal phValue, Geometry geom) {
        this.gid = gid;
        this.area = area;
        this.perimeter = perimeter;
        this.bount_ = bount_;
        this.bountId = bountId;
        this.gbcode = gbcode;
        this.adcode99 = adcode99;
        this.name99 = name99;
        this.sh2 = sh2;
        this.di2 = di2;
        this.x2 = x2;
        this.centroidY = centroidY;
        this.centroidX = centroidX;
        this.phValue = phValue;
        this.geom = geom;
    }

    @Override
    public String toString() {
        return "SanmingxianquPolygon{" +
                "gid=" + gid +
                ", area=" + (area != null ? area.toPlainString() : "null") +
                ", perimeter=" + (perimeter != null ? perimeter.toPlainString() : "null") +
                ", bount_=" + bount_ +
                ", bountId=" + bountId +
                ", gbcode='" + gbcode + '\'' +
                ", adcode99='" + adcode99 + '\'' +
                ", name99='" + name99 + '\'' +
                ", sh2='" + sh2 + '\'' +
                ", di2='" + di2 + '\'' +
                ", x2='" + x2 + '\'' +
                ", centroidY=" + (centroidY != null ? centroidY.toPlainString() : "null") +
                ", centroidX=" + (centroidX != null ? centroidX.toPlainString() : "null") +
                ", phValue=" + (phValue != null ? phValue.toPlainString() : "null") +
                ", geom=" + (geom != null ? geom.toText() : "null") + // 输出几何数据的 WKT 格式
                '}';
    }

}
