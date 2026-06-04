package org.example.service;

import java.util.List;

/**
 * 通用数据存储接口，所有业务层需实现此接口
 * @param <T> 模型类类型
 */
public interface DataStorageService<T>
{
    void saveAll(List<T> dataList); // 统一的批量存储方法
}
