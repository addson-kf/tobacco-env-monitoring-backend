package org.example.controller;

import org.example.service.IMonitoringPointImageService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

@RestController
@RequestMapping("/api/monitoring-points/{id}/images")
public class MonitoringPointImageController {
    private final IMonitoringPointImageService imageService;

    public MonitoringPointImageController(IMonitoringPointImageService imageService) {
        this.imageService = imageService;
    }

    /** 查询某监测点图片路径数组 */
    @GetMapping
    public List<String> list(@PathVariable("id") int id) {
        return imageService.listImagePaths(id);
    }

    /** 覆盖写入（用数组整体替换） */
    @PutMapping
    public void overwrite(@PathVariable("id") int id, @RequestBody List<String> paths) {
        imageService.overwriteImagePaths(id, paths);
    }

    /** 追加一条相对路径 */
    @PostMapping("/append")
    public void append(@PathVariable("id") int id, @RequestBody String relativePath) {
        imageService.appendOneImagePath(id, relativePath);
    }

    /** 删除一条相对路径 */
    @DeleteMapping
    public void remove(@PathVariable("id") int id, @RequestParam String relativePath) {
        imageService.removeOneImagePath(id, relativePath);
    }

    /** 按相对路径返回图片流（调试/下载用） */
    @GetMapping("/stream")
    public ResponseEntity<InputStreamResource> stream(@PathVariable("id") int id,
                                                      @RequestParam String relativePath) {
        InputStream is = imageService.openImageStream(id, relativePath);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + relativePath + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(is));
    }
    /** ★ 新增：上传图片（multipart），保存到磁盘并把相对路径追加到 ImagePaths */
    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String upload(@PathVariable("id") int id,
                         @RequestParam("file") MultipartFile file) throws Exception {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件为空");
        }

        // 1) 目录：monitoring-points/{id}/
        String relDir = "monitoring-points/" + id;
        Path dir = imageService.resolvePhysicalPath(relDir);
        Files.createDirectories(dir);

        // 2) 安全文件名：时间戳_原名（去掉斜杠等）
        String original = file.getOriginalFilename();
        if (original == null || original.isBlank()) original = "unnamed";
        original = original.replace("\\", "_").replace("/", "_");
        String filename = System.currentTimeMillis() + "_" + original;

        Path target = dir.resolve(filename).normalize();

        // 3) 先写磁盘，再写 DB；写库失败则删除刚写的文件
        try (InputStream in = file.getInputStream()) {
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            throw new RuntimeException("保存文件失败: " + target, e);
        }

        String relative = relDir + "/" + filename;
        try {
            imageService.appendOneImagePath(id, relative);
        } catch (Exception dbEx) {
            try { Files.deleteIfExists(target); } catch (Exception ignore) {}
            throw dbEx;
        }

        return relative; // 前端可直接使用该相对路径
    }
}
