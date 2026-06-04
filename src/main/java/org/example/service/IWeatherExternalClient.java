package org.example.service;

import java.util.Map;

public interface IWeatherExternalClient {
    /**
     * 按经纬度获取“当前实时天气”
     * 返回 current 字段 Map（键为外部字段名：temperature_2m 等）
     */
    Map<String, Object> fetchCurrent(double lat, double lon);
}
