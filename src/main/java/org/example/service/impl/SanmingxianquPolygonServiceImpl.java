package org.example.service.impl;

import org.example.dao.impl.SanmingxianquPolygonDaoImpl;
import org.example.dto.SanmingxianquPolygonDTO.SanmingxianquPolygonDTO;
import org.example.dto.SanmingxianquPolygonDTO.SanmingxianquPolygonListDTO;
import org.example.model.SanmingxianquPolygon;
import org.example.response.common.SanmingxianquPolygon.SanmingxianquPolygonListResponse;
import org.example.service.ISanmingxianquPolygonService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanmingxianquPolygonServiceImpl implements ISanmingxianquPolygonService
{
    @Autowired
    private SanmingxianquPolygonDaoImpl polygonDao; // 注入 DAO 层接口

    /**
     * 查询所有多边形数据并转换为 ListDTO
     */
    @Override
    @Transactional(readOnly = true)
    public SanmingxianquPolygonListResponse getAllPolygons() {
        try {
            List<SanmingxianquPolygon> entityList = polygonDao.getAllPolygons();
            List<SanmingxianquPolygonDTO> dtoList = entityList.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            SanmingxianquPolygonListDTO listDTO = new SanmingxianquPolygonListDTO(dtoList);
            return SanmingxianquPolygonListResponse.ok(listDTO); // 成功响应
        } catch (Exception e) {
            // 直接返回错误响应（注意：服务层依赖响应类，违反分层原则）
            return SanmingxianquPolygonListResponse.error("查询失败：" + e.getMessage());
        }
    }


    /**
     * 控制台输出所有多边形的 toString 信息
     */
    @Override
    @Transactional(readOnly = true) // 标记为只读事务
    public void printAllPolygonsToConsole() {
        try {
            // 调用 DAO 层获取实体列表
            List<SanmingxianquPolygon> entityList = polygonDao.getAllPolygons();

            // 遍历输出每个实体的 toString
            System.out.println("===== 三明县区多边形数据列表 =====");
            for (SanmingxianquPolygon polygon : entityList) {
                System.out.println(polygon.toString()); // 直接调用模型类的 toString 方法
            }
            System.out.println("===== 数据输出完毕 =====");
        } catch (Exception e) {
            System.err.println("输出数据时发生异常: " + e.getMessage());
        }
    }

    // ========== 辅助方法 ==========
    /**
     * 将实体类转换为 DTO（属性直接映射）
     */
    private SanmingxianquPolygonDTO convertToDTO(SanmingxianquPolygon entity) {
        SanmingxianquPolygonDTO dto = new SanmingxianquPolygonDTO();
        BeanUtils.copyProperties(entity, dto); // 使用 Spring BeanUtils 简化属性复制
        return dto;
    }
}
