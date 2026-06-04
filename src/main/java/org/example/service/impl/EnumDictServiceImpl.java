package org.example.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.dto.EnumDictDTO.EnumDictDTO;
import org.example.service.IEnumDictService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.ClassUtils;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;

import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class EnumDictServiceImpl implements IEnumDictService {

    @Value("${enumDict.externalPath}")
    private String externalPath;

    @Value("${enumDict.classpathFallback}")
    private String classpathFallback;

    @Value("${enumDict.enumBasePackage}")
    private String enumBasePackage;

    private final ObjectMapper om = new ObjectMapper();

    private final AtomicReference<Map<String, Map<String,String>>> i18nCache = new AtomicReference<>(new HashMap<>());
    private final AtomicReference<Map<String, List<String>>> enumsCache = new AtomicReference<>(new HashMap<>());
    private final AtomicLong lastLoadedExternalMtime = new AtomicLong(-1L);

    @Override
    public synchronized EnumDictDTO getEnumDict(boolean reloadIfChanged) {
        Map<String, List<String>> enums = scanEnumConstants(enumBasePackage);
        enumsCache.set(enums);

        Map<String, Map<String,String>> i18n = loadI18nDict(reloadIfChanged);

        Map<String, Map<String,String>> completed = new LinkedHashMap<>();
        for (var e : enums.entrySet()) {
            String enumName = e.getKey();
            List<String> constants = e.getValue();
            Map<String,String> m = i18n.getOrDefault(enumName, Collections.emptyMap());
            Map<String,String> filled = new LinkedHashMap<>();
            for (String k : constants) filled.put(k, m.getOrDefault(k, k));
            completed.put(enumName, filled);
        }

        EnumDictDTO dto = new EnumDictDTO();
        dto.setEnums(enums);
        dto.setI18n(completed);
        return dto;
    }

    @Override
    public synchronized void updateDict(Map<String, Map<String, String>> newDict) {
        Map<String, List<String>> enums = enumsCache.get();
        if (enums.isEmpty()) enums = scanEnumConstants(enumBasePackage);

        for (var entry : newDict.entrySet()) {
            String enumName = entry.getKey();
            if (!enums.containsKey(enumName)) {
                throw new IllegalArgumentException("未知的枚举：" + enumName);
            }
            Set<String> allowed = new HashSet<>(enums.get(enumName));
            for (String constName : entry.getValue().keySet()) {
                if (!allowed.contains(constName)) {
                    throw new IllegalArgumentException("枚举 " + enumName + " 中不存在常量：" + constName);
                }
            }
        }

        File target = new File(externalPath);
        target.getParentFile().mkdirs();
        File tmp = new File(target.getParentFile(), target.getName() + ".tmp");
        try (FileOutputStream fos = new FileOutputStream(tmp)) {
            om.writerWithDefaultPrettyPrinter().writeValue(fos, newDict);
        } catch (IOException e) {
            throw new RuntimeException("写入枚举字典失败", e);
        }
        try {
            Files.move(tmp.toPath(), target.toPath(),
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                    java.nio.file.StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            throw new RuntimeException("替换枚举字典失败", e);
        }
        lastLoadedExternalMtime.set(-1L);
    }

    private Map<String, List<String>> scanEnumConstants(String basePackage) {
        Map<String, List<String>> result = new LinkedHashMap<>();
        try {
            String pattern = "classpath*:" +
                    ClassUtils.convertClassNameToResourcePath(basePackage) + "/**/*.class";
            PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            CachingMetadataReaderFactory readerFactory = new CachingMetadataReaderFactory();

            Resource[] resources = resolver.getResources(pattern);
            for (Resource r : resources) {
                MetadataReader mr = readerFactory.getMetadataReader(r);
                String className = mr.getClassMetadata().getClassName();
                Class<?> clazz = Class.forName(className);
                if (clazz.isEnum()) {
                    @SuppressWarnings("unchecked")
                    Class<? extends Enum<?>> ec = (Class<? extends Enum<?>>) clazz;
                    List<String> names = Arrays.stream(ec.getEnumConstants())
                            .map(Enum::name).collect(Collectors.toList());
                    result.put(clazz.getSimpleName(), names);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("扫描枚举失败: " + basePackage, e);
        }
        return result;
    }

    private Map<String, Map<String, String>> loadI18nDict(boolean reloadIfChanged) {
        Map<String, Map<String,String>> cached = i18nCache.get();
        File f = new File(externalPath);
        long mtime = f.exists() ? f.lastModified() : -1L;

        boolean needReload = cached.isEmpty() || (reloadIfChanged && mtime != lastLoadedExternalMtime.get());
        if (!needReload) return cached;

        Map<String, Map<String,String>> merged = new LinkedHashMap<>();
        try (InputStream is = new ClassPathResource(classpathFallback).getInputStream()) {
            Map<String, Map<String,String>> base = om.readValue(is, new TypeReference<>() {});
            merged.putAll(base);
        } catch (IOException ignore) {}

        if (f.exists()) {
            try (InputStream is = new FileInputStream(f)) {
                Map<String, Map<String,String>> ext = om.readValue(is, new TypeReference<>() {});
                for (var e : ext.entrySet()) {
                    merged.put(e.getKey(), new LinkedHashMap<>(e.getValue()));
                }
            } catch (IOException ignore) {}
        }

        i18nCache.set(merged);
        lastLoadedExternalMtime.set(mtime);
        return merged;
    }

}
