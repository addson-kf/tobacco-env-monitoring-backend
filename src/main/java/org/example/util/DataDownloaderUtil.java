package org.example.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DataDownloaderUtil
{
    private static final Logger logger = LoggerFactory.getLogger(DataDownloaderUtil.class);
    private static volatile DataDownloaderUtil instance;
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private DataDownloaderUtil() {}

    public static DataDownloaderUtil getInstance() {
        if (instance == null) {
            synchronized (DataDownloaderUtil.class) {
                if (instance == null) {
                    instance = new DataDownloaderUtil();
                }
            }
        }
        return instance;
    }

    // 数据下载（保持不变）
    public String dataDownloader(String api, String cookie) {
        if (api == null || api.isEmpty()) {
            throw new IllegalArgumentException("API 地址不能为空");
        }
        if (cookie == null || cookie.isEmpty()) {
            throw new IllegalArgumentException("Cookie 不能为空");
        }
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(api))
                    .header("Cache-Control", "no-cache")
                    .header("Accept", "application/json")
                    .header("Cookie", cookie)
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .timeout(java.time.Duration.ofSeconds(30))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            logger.info("Status Code: {}", response.statusCode());
            return response.body();
        } catch (IOException | InterruptedException e) {
            throw new DataDownloadException("数据下载失败", e);
        }
    }

    // 数据解析（支持全类型转换）
    public <T> List<T> parseDeviceData(
            String jsonResponse,
            Class<T> elementType,
            Map<String, Map<String, String>> fieldMappings
    ) {
        try {
            List<Map<String, Object>> rawDataList = OBJECT_MAPPER.readValue(
                    jsonResponse,
                    TypeFactory.defaultInstance().constructCollectionType(List.class, Map.class)
            );

            List<T> mappedDataList = new ArrayList<>();
            for (Map<String, Object> rawData : rawDataList) {
                T instance = elementType.getDeclaredConstructor().newInstance();

                for (Map.Entry<String, Map<String, String>> entry : fieldMappings.entrySet()) {
                    String modelField = entry.getKey();
                    String dataField = entry.getValue().get("dataField");
                    String defaultValue = entry.getValue().getOrDefault("defaultValue", "");

                    Object value = rawData.get(dataField);
                    if (value == null) {
                        value = defaultValue;
                    }

                    Field field = elementType.getDeclaredField(modelField);
                    Class<?> fieldType = field.getType();
                    value = convertValue(fieldType, value, defaultValue); // 核心转换逻辑

                    String setterMethodName = "set" + capitalizeFirstLetter(modelField);
                    Method setterMethod = elementType.getMethod(setterMethodName, fieldType);
                    setterMethod.invoke(instance, value);
                }
                mappedDataList.add(instance);
            }
            return mappedDataList;
        } catch (Exception e) {
            logger.error("类型转换或字段映射失败: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // 核心类型转换逻辑
    private Object convertValue(Class<?> fieldType, Object value, String defaultValue) {
        try {
            if (fieldType == LocalDateTime.class) {
                return parseLocalDateTime(value.toString());
            } else if (fieldType == boolean.class || fieldType == Boolean.class) {
                return parseBoolean(value.toString());
            } else if (fieldType == int.class || fieldType == Integer.class) {
                return Integer.parseInt(value.toString());
            } else if (fieldType == long.class || fieldType == Long.class) {
                return Long.parseLong(value.toString());
            } else if (fieldType == double.class || fieldType == Double.class) {
                return Double.parseDouble(value.toString());
            } else if (fieldType == String.class) {
                return value.toString();
            }else if (fieldType.isEnum()) {
                Enum<?> enumValue = EnumMapperUtil.getInstance().mapValue(
                        fieldType.getName(),
                        value.toString()
                );
                // 若映射失败，使用默认值
                return (enumValue != null) ? enumValue : Enum.valueOf((Class<Enum>) fieldType, defaultValue);
            }
        } catch (Exception e) {
            logger.warn("类型转换失败，使用默认值: {}", defaultValue, e);
            return parseDefaultValue(fieldType, defaultValue);
        }
        return value;
    }

    // LocalDateTime 自动解析（支持多种格式）
    private LocalDateTime parseLocalDateTime(String dateString) {
        List<DateTimeFormatter> formatters = Arrays.asList(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"),
                DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ISO_LOCAL_DATE_TIME, // 新增 ISO 格式（含 T）
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS") // 新增带毫秒格式
        );
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new DateTimeParseException("不支持的日期格式", dateString, 0);
    }

    // boolean 自动转换（支持 1/0/true/false/是/否 等）
    private boolean parseBoolean(String boolString) {
        String lowerCase = boolString.toLowerCase();
        return Arrays.asList("1", "true", "yes", "y", "是", "对", "正确").contains(lowerCase);
    }


    // 默认值解析
    private Object parseDefaultValue(Class<?> fieldType, String defaultValue) {
        if (fieldType == LocalDateTime.class) {
            return parseLocalDateTime(defaultValue);
        } else if (fieldType == boolean.class || fieldType == Boolean.class) {
            return parseBoolean(defaultValue);
        } else if (fieldType.isEnum()) {
            return Enum.valueOf((Class<Enum>) fieldType, defaultValue);
        } else if (fieldType == int.class || fieldType == Integer.class) {
            return Integer.parseInt(defaultValue);
        } else if (fieldType == long.class || fieldType == Long.class) {
            return Long.parseLong(defaultValue);
        } else if (fieldType == double.class || fieldType == Double.class) {
            return Double.parseDouble(defaultValue);
        } else if (fieldType == String.class) {
            return defaultValue;
        }
        return null;
    }

    private String capitalizeFirstLetter(String field) {
        return field.substring(0, 1).toUpperCase() + field.substring(1);
    }

    class DataDownloadException extends RuntimeException {
        public DataDownloadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}