package org.example.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface EnumIntrospectMapper {
    List<String> listEnumLabels(@Param("typname") String typname);
}
