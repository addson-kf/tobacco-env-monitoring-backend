package org.example.service.impl;

import org.example.mapper.MonitoringPointMapper;
import org.example.model.MonitoringPoint;
import org.example.service.IMonitoringPointImageService;
import org.example.service.IMonitoringPointService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class MonitoringPointImageServiceImpl implements IMonitoringPointImageService {

    private final MonitoringPointMapper mapper;

    // ② 用 @Value 注入；支持三层优先：yml -> 环境变量 FILES_BASE_DIR -> 默认值
    @Value("${files.base-dir:${FILES_BASE_DIR:}}")
    private String baseDir;

    // 兜底默认值（仅当上面都没注入时才用）
    private static final String DEFAULT_BASE_DIR = "E:/tobacco_images";


    public MonitoringPointImageServiceImpl(MonitoringPointMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<String> listImagePaths(int monitoringPointId) {
        // 方案1：直接查数组（如果你按A-2写的是 resultType=List，直接返回）
        List<String> paths = mapper.selectImagePathsById(monitoringPointId);
        return paths == null ? java.util.Collections.emptyList() : paths;

        // 方案2（更稳）：如果A-2里把select写成resultMap返回MonitoringPoint，
        // MonitoringPoint mp = mapper.selectOneById(monitoringPointId); // 若你改成这个名字
        // return (mp == null || mp.getImagePaths() == null) ? Collections.emptyList() : mp.getImagePaths();
    }

    @Override
    public void overwriteImagePaths(int monitoringPointId, List<String> paths) {
        mapper.updateImagePaths(monitoringPointId, paths == null ? java.util.Collections.emptyList() : paths);
    }

    @Override
    public void appendOneImagePath(int monitoringPointId, String path) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path 不能为空");
        if (path.contains("..")) throw new SecurityException("非法路径");
        mapper.appendOneImagePath(monitoringPointId, path);
    }

    @Override
    public void removeOneImagePath(int monitoringPointId, String path) {
        if (path == null || path.isBlank()) throw new IllegalArgumentException("path 不能为空");
        mapper.removeOneImagePath(monitoringPointId, path);
    }

    @Override
    public InputStream openImageStream(int monitoringPointId, String relativePath) {
        if (relativePath == null || relativePath.isBlank()) throw new IllegalArgumentException("relativePath 不能为空");
        if (relativePath.contains("..")) throw new SecurityException("非法路径");
        java.nio.file.Path p = resolvePhysicalPath(relativePath);
        try {
            return java.nio.file.Files.newInputStream(p, java.nio.file.StandardOpenOption.READ);
        } catch (java.io.IOException e) {
            throw new RuntimeException("读取图片失败: " + p, e);
        }
    }

    @Override
    public java.nio.file.Path resolvePhysicalPath(String relativePath) {
        return java.nio.file.Paths.get(baseDir).resolve(relativePath).normalize();
    }

}
