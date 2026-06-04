package org.example.service.impl;

import org.example.dto.ExcelImportRequestDTO.ExcelImportRequestDTO;
import org.example.response.base.InsertResponse;
import org.example.util.UniversalExcelImporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Service
public class ExcelImportService
{
    private final UniversalExcelImporter excelImporter;

    @Autowired
    public ExcelImportService(UniversalExcelImporter excelImporter) {
        this.excelImporter = excelImporter;
    }

    /**
     * 处理Excel导入请求
     * @param request 包含Excel文件和XML配置的请求DTO
     * @return 导入结果响应
     */
    @Transactional
    public InsertResponse importExcelData(ExcelImportRequestDTO request) {
        try {
            // 校验请求参数
            validateRequest(request);

            // 获取Excel文件和配置
            MultipartFile excelFile = request.getExcelFile();
            String configXml = request.getConfigXml();

            // 处理Excel导入
            processExcelImport(excelFile, configXml);

            return InsertResponse.ok("Excel数据导入成功");
        } catch (Exception e) {
            return InsertResponse.error("Excel数据导入失败: " + e.getMessage());
        }
    }

    /**
     * 校验导入请求
     */
    private void validateRequest(ExcelImportRequestDTO request) throws IllegalArgumentException {
        if (request == null) {
            throw new IllegalArgumentException("导入请求不能为空");
        }

        if (request.getExcelFile() == null || request.getExcelFile().isEmpty()) {
            throw new IllegalArgumentException("Excel文件不能为空");
        }

        if (request.getConfigXml() == null || request.getConfigXml().trim().isEmpty()) {
            throw new IllegalArgumentException("XML配置不能为空");
        }
    }

    /**
     * 执行Excel导入逻辑
     */
    private void processExcelImport(MultipartFile excelFile, String configXml) throws Exception {
        try (InputStream excelStream = excelFile.getInputStream()) {
            // 调用工具类进行导入
            excelImporter.batchImportFromStream(configXml, excelStream);
        } catch (IOException e) {
            throw new Exception("读取Excel文件失败", e);
        }
    }
}
