package org.example.service.util;

import java.util.*;

public class EnumLabelResolver {
    private EnumLabelResolver() {}

    // 这里先放常用别名；按你的真实枚举名随时补充/修改
    private static final Map<String, Map<String, String>> ALIAS = new HashMap<>();
    static {
        Map<String,String> soil = new HashMap<>();
        soil.put("有机质", "OrganicMatter");  // ⚠️ 按你DB里的真实字面量改
        soil.put("ph", "PH"); soil.put("pH", "PH"); soil.put("PH", "PH");

        // soil.put("速效钾", "AvailableK"); ... 需要就继续补
        ALIAS.put("soil", soil);

        Map<String,String> enzyme = new HashMap<>();

        // enzyme.put("脲酶", "Urease"); ...
        ALIAS.put("enzyme", enzyme);

        Map<String,String> leaf = new HashMap<>();
        // leaf.put("糖分", "Sugar"); ...
        ALIAS.put("leaf", leaf);

        Map<String,String> weather = new HashMap<>();
        // weather.put("降雨量", "Rainfall"); ...
        ALIAS.put("weather", weather);
    }

    public static String normalize(String dataset, String input, List<String> dbLabels) {
        if (input == null) return null;
        // 1) 精确匹配
        for (String l : dbLabels) if (l.equals(input)) return l;
        // 2) 忽略大小写
        for (String l : dbLabels) if (l.equalsIgnoreCase(input)) return l;
        // 3) 中文/别名映射
        Map<String,String> dict = ALIAS.getOrDefault(dataset.toLowerCase(Locale.ROOT), Map.of());
        String mapped = dict.get(input);
        if (mapped != null) {
            // 确认映射结果确实存在于DB枚举
            for (String l : dbLabels) if (l.equals(mapped)) return l;
        }
        // 4) 都不匹配，报清晰的错误
        throw new IllegalArgumentException("属性不合法: " + input + "；可选值: " + dbLabels);
    }
}
