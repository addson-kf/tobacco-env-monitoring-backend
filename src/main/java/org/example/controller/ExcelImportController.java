package org.example.controller;

import org.example.dto.ExcelImportRequestDTO.ExcelImportRequestDTO;
import org.example.response.base.InsertResponse;
import org.example.service.impl.ExcelImportService;
import org.example.util.UniversalExcelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/import")
@CrossOrigin(origins = "*")
public class ExcelImportController
{
    private final ExcelImportService importService;
    private final UniversalExcelImporter excelImporter; // 注入工具类

    @Autowired
    public ExcelImportController(ExcelImportService importService,
                                 UniversalExcelImporter excelImporter) {
        this.importService = importService;
        this.excelImporter = excelImporter;
    }

    @PostMapping("/excel")
    public InsertResponse importExcel(ExcelImportRequestDTO request) {
        try {
            MultipartFile excelFile = request.getExcelFile();
            String configXml = request.getConfigXml();

            // 验证文件和配置
            if (excelFile.isEmpty()) {
                return InsertResponse.error("Excel文件不能为空");
            }
            if (configXml == null || configXml.trim().isEmpty()) {
                return InsertResponse.error("XML配置不能为空");
            }

            // 调用业务层或直接调用工具类
            importService.importExcelData(request); // 推荐通过业务层

            return InsertResponse.ok("Excel数据导入成功");
        } catch (Exception e) {
            return InsertResponse.error("Excel数据导入失败: " + e.getMessage());
        }
    }
}
