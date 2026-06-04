package org.example.model.downloadtest.test;

public enum DeviceIdEnum
{
    DEVICE_1001("d-1001-pbcevsqqrslh-1-105"),
    DEVICE_1002("d-1002-abcdefghijkl-2-106"),
    DEVICE_1003("d-1003-xyzabcdefgh-3-107"),
    UNKNOWN("unknown"); // 默认值

    private final String sensorValue;

    DeviceIdEnum(String sensorValue) {
        this.sensorValue = sensorValue;
    }

    // 根据传感器值获取枚举
    public static DeviceIdEnum fromSensorValue(String sensorValue) {
        for (DeviceIdEnum enumValue : DeviceIdEnum.values()) {
            if (enumValue.sensorValue.equals(sensorValue)) {
                return enumValue;
            }
        }
        return UNKNOWN; // 默认返回 UNKNOWN
    }
}
