package org.example.model.downloadtest;

import org.example.model.downloadtest.test.DeviceIdEnum;

import java.time.LocalDateTime;

public class DeviceData
{
    private double dataValue;       // 映射 JSON 的 "value"
    private long timestamp;        // 映射 JSON 的 "t"
    private DeviceIdEnum deviceId;       // 映射 JSON 的 "agri_id"
    private LocalDateTime displayTime;    // 映射 JSON 的 "t_display"

    // 无参构造函数（必需）
    public DeviceData() {}

    // 带参构造函数（可选，用于日志输出）
    public DeviceData(double dataValue, long timestamp, DeviceIdEnum deviceId, LocalDateTime displayTime) {
        this.dataValue = dataValue;
        this.timestamp = timestamp;
        this.deviceId = deviceId;
        this.displayTime = displayTime;
    }

    // Setter 方法（必需，参数名与模型字段一致）
    public void setDataValue(double dataValue) {
        this.dataValue = dataValue;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public void setDeviceId(DeviceIdEnum deviceId) {
        this.deviceId = deviceId;
    }

    public void setDisplayTime(LocalDateTime displayTime) {
        this.displayTime = displayTime;
    }

    // Getter 方法
    public double getDataValue() {
        return dataValue;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public DeviceIdEnum getDeviceId() {
        return deviceId;
    }

    public LocalDateTime getDisplayTime() {
        return displayTime;
    }

    // 重写 toString 方法（方便日志输出）
    @Override
    public String toString() {
        return "DeviceData{" +
                "dataValue=" + dataValue +
                ", timestamp=" + timestamp +
                ", deviceId='" + deviceId + '\'' +
                ", displayTime='" + displayTime + '\'' +
                '}';
    }
}
