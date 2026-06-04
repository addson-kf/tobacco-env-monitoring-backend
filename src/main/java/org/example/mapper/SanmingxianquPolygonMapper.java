package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.SanmingxianquPolygon;

import java.util.List;

@Mapper
public interface SanmingxianquPolygonMapper
{
    /**
     * 查询所有记录
     * @return 所有SanmingxianquPolygon对象列表
     */
    List<SanmingxianquPolygon> findAll();
}
