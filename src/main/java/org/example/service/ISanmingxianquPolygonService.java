package org.example.service;

import org.example.dto.SanmingxianquPolygonDTO.SanmingxianquPolygonListDTO;
import org.example.response.common.SanmingxianquPolygon.SanmingxianquPolygonListResponse;

public interface ISanmingxianquPolygonService
{
    /**
     * 查询所有多边形数据，返回封装后的 ListDTO
     * @return SanmingxianquPolygonListDTO 包含多边形 DTO 列表
     */
    SanmingxianquPolygonListResponse getAllPolygons();

    /**
     * 控制台输出所有多边形数据的 toString 信息
     */
    void printAllPolygonsToConsole();
}
