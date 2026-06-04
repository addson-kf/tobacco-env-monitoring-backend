package org.example.service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

public interface IMonitoringPointImageService {
    List<String> listImagePaths(int monitoringPointId);

    void overwriteImagePaths(int monitoringPointId, List<String> paths);

    void appendOneImagePath(int monitoringPointId, String path);

    void removeOneImagePath(int monitoringPointId, String path);

    //图片
    java.io.InputStream openImageStream(int monitoringPointId, String relativePath);
    /** 把相对路径转换为物理磁盘路径（基于 files.base-dir） */
    java.nio.file.Path resolvePhysicalPath(String relativePath);


}
