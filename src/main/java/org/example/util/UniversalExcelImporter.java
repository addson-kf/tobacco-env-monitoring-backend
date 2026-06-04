package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.service.DataStorageService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class UniversalExcelImporter implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private final Map<String, Object> serviceCache = new ConcurrentHashMap<>();

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * 前端上传场景：从XML字符串和文件流导入
     */
    public void batchImportFromStream(String configXml, InputStream excelStream) throws Exception {
        List<ExcelConfig> configs = parseXmlConfigFromString(configXml);
        for (ExcelConfig config : configs) {
            processExcelFile(config, excelStream);
        }
    }

    /**
     * 原有文件路径导入场景
     */
    public void batchImport(String xmlPath) throws Exception {
        List<ExcelConfig> configs = parseXmlConfig(xmlPath);
        for (ExcelConfig config : configs) {
            processExcelFile(config);
        }
    }

    private void processExcelFile(ExcelConfig config) throws Exception {
        try (FileInputStream fis = new FileInputStream(config.filePath);
             Workbook workbook = new XSSFWorkbook(fis)) {
            processExcelFile(config, workbook);
        }
    }

    private void processExcelFile(ExcelConfig config, InputStream excelStream) throws Exception {
        try (Workbook workbook = new XSSFWorkbook(excelStream)) {
            processExcelFile(config, workbook);
        }
    }

    private void processExcelFile(ExcelConfig config, Workbook workbook) throws Exception {
        Class<?> modelClass = Class.forName(config.modelClass);
        List<?> dataList = readExcelData(config, modelClass, workbook);
        saveToDatabase(config, dataList);
    }

    private List<ExcelConfig> parseXmlConfig(String xmlPath) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        try (InputStream inputStream = getClass().getResourceAsStream(xmlPath)) {
            org.w3c.dom.Document doc = builder.parse(inputStream);
            return parseExcelConfigs(doc);
        }
    }

    private List<ExcelConfig> parseXmlConfigFromString(String configXml) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(configXml));
        org.w3c.dom.Document doc = builder.parse(inputSource);
        return parseExcelConfigs(doc);
    }

    private List<ExcelConfig> parseExcelConfigs(org.w3c.dom.Document doc) {
        NodeList excelFiles = doc.getElementsByTagName("excelFile");
        return IntStream.range(0, excelFiles.getLength())
                .mapToObj(excelFiles::item)
                .map(this::convertToExcelConfig)
                .collect(Collectors.toList());
    }

    private ExcelConfig convertToExcelConfig(Node node) {
        ExcelConfig config = new ExcelConfig();
        if (node.getNodeType() != Node.ELEMENT_NODE) return config;

        org.w3c.dom.Element element = (org.w3c.dom.Element) node;
        config.filePath = getElementText(element, "filePath");
        config.modelClass = getElementText(element, "modelClass");
        config.serviceClass = getElementText(element, "serviceClass");
        config.sheetName = getSheetName(element);
        config.headerRowIndex = getHeaderRowIndex(element);
        config.dataStartRowIndex = getDataStartRowIndex(element);
        config.fieldMappings = parseFieldMappings(element);
        return config;
    }

    private String getSheetName(org.w3c.dom.Element element) {
        NodeList sheets = element.getElementsByTagName("sheet");
        return sheets.getLength() > 0 ?
                ((org.w3c.dom.Element)sheets.item(0)).getAttribute("name") : "Sheet1";
    }

    private int getHeaderRowIndex(org.w3c.dom.Element element) {
        NodeList sheets = element.getElementsByTagName("sheet");
        return sheets.getLength() > 0 ?
                parseIntAttribute((org.w3c.dom.Element)sheets.item(0), "headerRowIndex", 0) : 0;
    }

    private int getDataStartRowIndex(org.w3c.dom.Element element) {
        NodeList sheets = element.getElementsByTagName("sheet");
        return sheets.getLength() > 0 ?
                parseIntAttribute((org.w3c.dom.Element)sheets.item(0), "dataStartRowIndex", 1) : 1;
    }

    private int parseIntAttribute(org.w3c.dom.Element element, String attrName, int defaultValue) {
        String value = element.getAttribute(attrName);
        return !value.isEmpty() ? Integer.parseInt(value) : defaultValue;
    }

    private List<FieldMapping> parseFieldMappings(org.w3c.dom.Element element) {
        NodeList fields = element.getElementsByTagName("field");
        return IntStream.range(0, fields.getLength())
                .mapToObj(fields::item)
                .map(this::convertToFieldMapping)
                .collect(Collectors.toList());
    }

    private FieldMapping convertToFieldMapping(Node node) {
        FieldMapping mapping = new FieldMapping();
        if (node.getNodeType() != Node.ELEMENT_NODE) return mapping;

        org.w3c.dom.Element fieldElement = (org.w3c.dom.Element) node;
        mapping.excelHeader = fieldElement.getAttribute("excelHeader");
        mapping.modelField = fieldElement.getAttribute("modelField");
        mapping.dataType = fieldElement.getAttribute("type").toUpperCase(Locale.ROOT);
        mapping.enumClass = fieldElement.getAttribute("enumClass");
        mapping.enumValue = fieldElement.getAttribute("enumValue");
        mapping.trueValue = fieldElement.getAttribute("trueValue");
        mapping.falseValue = fieldElement.getAttribute("falseValue");
        mapping.defaultValue = fieldElement.getAttribute("defaultValue");

        // 初始化默认值
        initDefaultValues(mapping);
        return mapping;
    }

    private void initDefaultValues(FieldMapping mapping) {
        if (mapping.dataType.isEmpty()) mapping.dataType = "STRING";
        if (mapping.trueValue.isEmpty()) mapping.trueValue = "是";
        if (mapping.falseValue.isEmpty()) mapping.falseValue = "否";
        if (mapping.defaultValue == null) mapping.defaultValue = "";
    }

    private String getElementText(org.w3c.dom.Element element, String tagName) {
        NodeList nodes = element.getElementsByTagName(tagName);
        return nodes.getLength() > 0 ? nodes.item(0).getTextContent().trim() : "";
    }

    private <T> List<T> readExcelData(ExcelConfig config, Class<T> modelClass, Workbook workbook) throws Exception {
        List<T> dataList = new ArrayList<>();
        Sheet sheet = workbook.getSheet(config.sheetName);
        if (sheet == null) throw new IllegalArgumentException("工作表未找到: " + config.sheetName);

        Map<String, Integer> headerMap = buildHeaderMap(sheet.getRow(config.headerRowIndex));

        // 基础信息字段（烤房编号、农户姓名等）
        List<FieldMapping> baseFieldMappings = config.fieldMappings.stream()
                .filter(m -> !m.getModelField().equals("soilAttribute") && !m.getModelField().equals("value"))
                .collect(Collectors.toList());

        // 属性字段（pH值、交换性钙等）
        Map<String, List<FieldMapping>> attributeMappings = config.fieldMappings.stream()
                .filter(m -> m.getModelField().equals("soilAttribute") || m.getModelField().equals("value"))
                .collect(Collectors.groupingBy(FieldMapping::getExcelHeader));

        for (int rowNum = config.dataStartRowIndex; rowNum <= sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row == null) continue;

            // 为每个属性创建一条记录
            for (Map.Entry<String, List<FieldMapping>> entry : attributeMappings.entrySet()) {
                String excelHeader = entry.getKey();
                List<FieldMapping> mappings = entry.getValue();

                Integer columnIndex = headerMap.get(excelHeader);
                if (columnIndex == null) {
                    System.out.println("跳过属性，未找到列：" + excelHeader);
                    continue;
                }

                Cell valueCell = row.getCell(columnIndex);
                if (valueCell == null || isCellEmpty(valueCell)) {
                    System.out.println("跳过空属性，列：" + excelHeader);
                    continue;
                }

                // 创建新记录
                T model = createModelInstance(modelClass);

                // 应用基础信息
                for (FieldMapping baseMapping : baseFieldMappings) {
                    Integer baseColumnIndex = headerMap.get(baseMapping.getExcelHeader());
                    if (baseColumnIndex != null) {
                        applySingleMapping(model, baseMapping, row, baseColumnIndex);
                    }
                }

                // 应用属性信息
                for (FieldMapping mapping : mappings) {
                    applySingleMapping(model, mapping, row, columnIndex);
                }

                dataList.add(model);
            }
        }

        return dataList;
    }

    private <T> void applyMappings(T model, List<FieldMapping> mappings, Row row, Integer columnIndex, Map<String, Integer> headerMap) throws Exception {
        for (FieldMapping mapping : mappings) {
            // 统一使用传入的 headerMap 获取列索引
            Integer fieldColumnIndex = headerMap.get(mapping.getExcelHeader());
            if (fieldColumnIndex != null) {
                applySingleMapping(model, mapping, row, fieldColumnIndex);
            } else {
                System.out.println("警告: 未找到列索引，跳过映射 - " + mapping.getExcelHeader());
            }
        }
    }


    private Map<String, Integer> buildHeaderMap(Row headerRow) {
        Map<String, Integer> headerMap = new HashMap<>();
        for (int i = 0; i < headerRow.getPhysicalNumberOfCells(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String header = cell.getStringCellValue().trim();
                headerMap.put(header, i);
            }
        }
        return headerMap;
    }

    private Map<String, List<FieldMapping>> buildAttributeMappings(List<FieldMapping> mappings) {
        return mappings.stream()
                .filter(m -> m.enumValue != null && !m.enumValue.isEmpty())
                .collect(Collectors.groupingBy(m -> m.excelHeader));
    }

    private List<FieldMapping> getCommonMappings(List<FieldMapping> mappings) {
        return mappings.stream()
                .filter(m -> m.enumValue == null || m.enumValue.isEmpty())
                .collect(Collectors.toList());
    }

    private <T> T createModelInstance(Class<T> modelClass) throws Exception {
        Constructor<T> constructor = modelClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private <T> void applySingleMapping(T model, FieldMapping mapping, Row row, Integer columnIndex) throws Exception {
        Cell cell = row.getCell(columnIndex);
        Field field = model.getClass().getDeclaredField(mapping.modelField);
        field.setAccessible(true);

        Object value = convertCellToValue(cell, mapping);
        field.set(model, value);
    }

    private <T> void applyCommonMappings(T model, List<FieldMapping> mappings, Row row, Map<String, Integer> headerMap) throws Exception {
        for (FieldMapping mapping : mappings) {
            Integer columnIndex = headerMap.get(mapping.getExcelHeader());
            if (columnIndex != null) {
                applySingleMapping(model, mapping, row, columnIndex);
            }
        }
    }

    private <T> void applyAttributeMappings(T model, List<FieldMapping> mappings, Row row, Map<String, Integer> headerMap) throws Exception {
        for (FieldMapping mapping : mappings) {
            Integer columnIndex = headerMap.get(mapping.getExcelHeader());
            if (columnIndex != null) {
                applySingleMapping(model, mapping, row, columnIndex);
            }
        }
    }

    private Object convertCellToValue(Cell cell, FieldMapping mapping) {
        if (cell == null || isCellEmpty(cell)) {
            System.out.println("字段 [" + mapping.modelField + "] 为空，应用默认值：" + mapping.defaultValue);
            return parseDefaultValue(mapping);
        }

        String cellValue = extractCellValueAsString(cell);
        if (cellValue.isEmpty()) {
            System.out.println("字段 [" + mapping.modelField + "] 值为空字符串，应用默认值：" + mapping.defaultValue);
            return parseDefaultValue(mapping);
        }

        try {
            switch (mapping.dataType) {
                case "ENUM":
                    return getEnumValue(mapping);
                case "STRING":
                    return cellValue;
                case "INTEGER":
                    return Integer.parseInt(cellValue);
                case "DOUBLE":
                    return Double.parseDouble(cellValue);
                case "BOOLEAN":
                    return cellValue.equalsIgnoreCase(mapping.trueValue);
                case "LOCALDATETIME":
                    return getLocalDateTimeValue(cell);
                default:
                    return cellValue;
            }
        } catch (NumberFormatException e) {
            System.err.println("字段 [" + mapping.modelField + "] 格式错误，值：" + cellValue + "，应用默认值");
            return parseDefaultValue(mapping);
        } catch (Exception e) {
            throw new IllegalStateException("字段 [" + mapping.modelField + "] 转换失败", e);
        }
    }

    private boolean isCellEmpty(Cell cell) {
        return cell.getCellType() == CellType.BLANK ||
                (cell.getCellType() == CellType.STRING && cell.getStringCellValue().trim().isEmpty());
    }

    private String extractCellValueAsString(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return getFormulaResult(cell);
            default:
                return "";
        }
    }

    private String getFormulaResult(Cell cell) {
        switch (cell.getCachedFormulaResultType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return new SimpleDateFormat("yyyy-MM-dd").format(cell.getDateCellValue());
                } else {
                    return String.valueOf(cell.getNumericCellValue());
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }

    private Object parseDefaultValue(FieldMapping mapping) {
        String defaultValue = mapping.defaultValue;
        if (defaultValue.isEmpty()) {
            return getTypeDefaultValue(mapping.dataType);
        }

        try {
            switch (mapping.dataType) {
                case "BOOLEAN":
                    return Boolean.parseBoolean(defaultValue);
                case "INTEGER":
                    return Integer.parseInt(defaultValue);
                case "DOUBLE":
                    return Double.parseDouble(defaultValue);
                case "ENUM":
                    return Enum.valueOf((Class<Enum>) Class.forName(mapping.enumClass), defaultValue);
                default:
                    return defaultValue;
            }
        } catch (Exception e) {
            System.err.println("默认值解析失败，使用类型默认值：" + defaultValue);
            return getTypeDefaultValue(mapping.dataType);
        }
    }

    private Object getTypeDefaultValue(String dataType) {
        switch (dataType) {
            case "INTEGER":
                return 0;
            case "DOUBLE":
                return 0.0;
            case "BOOLEAN":
                return false;
            case "ENUM":
                return null;
            default:
                return "";
        }
    }

    private Object getEnumValue(FieldMapping mapping) throws ClassNotFoundException {
        if (mapping.enumValue != null && !mapping.enumValue.isEmpty()) {
            return Enum.valueOf((Class<Enum>) Class.forName(mapping.enumClass), mapping.enumValue);
        }
        return null;
    }

    private LocalDateTime getLocalDateTimeValue(Cell cell) {
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            return LocalDateTime.ofInstant(date.toInstant(), TimeZone.getDefault().toZoneId());
        }
        throw new IllegalStateException("字段必须为日期类型");
    }

    @SuppressWarnings("unchecked")
    private <T> void saveToDatabase(ExcelConfig config, List<?> dataList) throws Exception {
        DataStorageService<T> service = (DataStorageService<T>) serviceCache.computeIfAbsent(
                config.serviceClass,
                serviceName -> applicationContext.getBean(serviceName)
        );

        Method saveMethod = service.getClass().getMethod("saveAll", List.class);
        saveMethod.invoke(service, dataList);
    }

    // 配置类
    private static class ExcelConfig {
        String filePath;
        String modelClass;
        String serviceClass;
        String sheetName;
        int headerRowIndex;
        int dataStartRowIndex;
        List<FieldMapping> fieldMappings;
    }

    // 字段映射类
    private static class FieldMapping {
        String excelHeader;
        String modelField;
        String dataType;
        String enumClass;
        String enumValue;
        String trueValue;
        String falseValue;
        String defaultValue;

        // 添加所有字段的 getter 方法
        public String getExcelHeader() {
            return excelHeader;
        }

        public String getModelField() {
            return modelField;
        }

        public String getDataType() {
            return dataType;
        }

        public String getEnumClass() {
            return enumClass;
        }

        public String getEnumValue() {
            return enumValue;
        }

        public String getTrueValue() {
            return trueValue;
        }

        public String getFalseValue() {
            return falseValue;
        }

        public String getDefaultValue() {
            return defaultValue;
        }
    }
}
