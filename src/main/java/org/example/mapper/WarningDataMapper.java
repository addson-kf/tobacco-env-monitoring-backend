package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.model.downloadtest.WarningData;

import java.util.List;

@Mapper
public interface WarningDataMapper
{
    List<WarningData> findAll();

    void insertWarningData(WarningData warningData);
}
