package org.example.controller;

import org.example.config.ApiConfig;
import org.example.dto.SanmingxianquPolygonDTO.SanmingxianquPolygonListDTO;
import org.example.service.ISanmingxianquPolygonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.response.common.SanmingxianquPolygon.SanmingxianquPolygonListResponse;

/**
 * 三明县区多边形数据控制器，处理多边形数据相关请求。
 */
@RestController
@RequestMapping("#{@apiConfig.sanMingXianquPolygon.basePath}")
@CrossOrigin(origins = "*")
public class SanmingxianquPolygonController
{
    @Autowired
    private ISanmingxianquPolygonService polygonService;

    @Autowired
    private ApiConfig apiConfig;

    /**
     * 获取所有多边形数据
     */
    @GetMapping("#{@apiConfig.sanMingXianquPolygon.endpoints.all}")
    public SanmingxianquPolygonListResponse getAllPolygons() {
        return polygonService.getAllPolygons();
    }
}
