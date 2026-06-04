package org.example.service.impl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;
import org.example.dto.calc.CalcRequest;
import org.example.dto.calc.CalcResultRow;
import org.example.mapper.AnalysisMapper;
import org.example.mapper.SoilDataCalcMapper;
import org.example.mapper.SoilDataMapper;
import org.example.service.ICalculationService;
import org.springframework.stereotype.Service;
import org.example.service.IEnumDictService;
import org.example.dto.EnumDictDTO.EnumDictDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;
import org.example.dto.analysis.MaxminDTO;


import java.util.stream.Collectors;
import java.util.*;

@Service
@RequiredArgsConstructor
public class CalculationServiceImpl implements ICalculationService {
    private final SoilDataCalcMapper soilDataCalcMapper;  // 数据查询
    private final IEnumDictService enumDictService;


    @Override
    public List<CalcResultRow> calculate(CalcRequest req) {
        // —— 0) 基础校验 —— //
        final String formula = Optional.ofNullable(req.getFormula()).orElse("").trim();
        if (formula.isEmpty()) throw badRequest("公式不能为空");

// 受支持变量：优先用 getAttributeDict()，否则退回到内置 SUPPORTED_ATTRS
        final Set<String> supported = new LinkedHashSet<>();
        Map<String, String> dict = Optional.ofNullable(getAttributeDict()).orElse(Map.of());
        if (!dict.isEmpty()) supported.addAll(dict.keySet()); else supported.addAll(SUPPORTED_ATTRS);

// 从公式中提取实际出现的变量（只在 supported 集合内匹配）
        final Set<String> inFormula = extractUsedAttributes(formula, supported);

// 【统一构建 attributes：公式出现 ∪ 前端显式传入；保序去重；仅保留受支持的】
        LinkedHashSet<String> attrSet = new LinkedHashSet<>();

// 1) 先放“公式里实际出现的变量”（保证顺序更贴近公式，前端表头也直观）
        inFormula.stream()
                .filter(supported::contains)
                .forEach(attrSet::add);

// 2) 再把前端传来的 attributes 并进来（有就补充，没有也不影响）
        Optional.ofNullable(req.getAttributes())
                .orElseGet(Collections::emptyList)
                .stream()
                .filter(supported::contains)
                .forEach(attrSet::add);

// 3) 落成最终列表
        List<String> attributes = new ArrayList<>(attrSet);

// 4) 纯常量公式：不查库，直接计算一行返回
        if (attributes.isEmpty()) {
            double val = new ExpressionBuilder(formula).build().evaluate();
            double rounded = round2(val);
            DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            CalcResultRow row = new CalcResultRow(
                    null, // 无监测点意义
                    LocalDateTime.now().format(fmt),
                    Collections.emptyMap(),
                    rounded
            );
            return List.of(row);
        }


        // —— 1) 只查需要的属性 —— //
        List<Map<String, Object>> rawData = soilDataCalcMapper.queryForCalc(
                req.getTobaccoFieldType(),
                req.getCountyDistrict(),
                req.getTown(),
                req.getVillage(),
                req.getSamplingDateFrom(),
                req.getSamplingDateTo(),
                attributes
        );

        // —— 2) 分组：监测点 + 采样日期 —— //
        Map<Key, Map<String, Double>> groupedData = new LinkedHashMap<>();
        for (Map<String, Object> row : rawData) {
            Integer mpId = castInt(getFieldIgnoreCase(row, "monitoringPointID"));
            LocalDateTime dt = castDateTime(getFieldIgnoreCase(row, "samplingDate"));
            String attr = Objects.toString(getFieldIgnoreCase(row, "soilAttribute"), null);
            Double value = castDouble(getFieldIgnoreCase(row, "value"));
            if (mpId == null || dt == null || attr == null || value == null) continue;

            Key key = new Key(mpId, dt);
            groupedData.computeIfAbsent(key, k -> new HashMap<>()).put(attr, value);
        }

        // —— 3) 安全求值（exp4j）：限定变量集合，逐组设置变量 —— //
        ExpressionBuilder builder = new ExpressionBuilder(formula).variables(new HashSet<>(attributes));
        DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

        List<CalcResultRow> resultRows = new ArrayList<>();
        for (Map.Entry<Key, Map<String, Double>> entry : groupedData.entrySet()) {
            Map<String, Double> values = entry.getValue();

            // 仅保留用到的变量：缺的变量用 0（或你认可的默认值）
            Map<String, Double> usedValues = new LinkedHashMap<>();
            for (String a : attributes) {
                Double v = values.get(a);
                usedValues.put(a, v != null ? v : 0.0);
            }

            Expression expression = builder.build();
            // 再次保证每个变量都有值（防范 null）
            for (Map.Entry<String, Double> e : usedValues.entrySet()) {
                expression.setVariable(e.getKey(), e.getValue() != null ? e.getValue() : 0.0);
            }

            double result = expression.evaluate();
            if (Double.isNaN(result) || Double.isInfinite(result)) {
                result = 0.0; // 防异常
            }
            double rounded = round2(result);

            resultRows.add(new CalcResultRow(
                    entry.getKey().getMpId(),
                    entry.getKey().getSamplingDate().format(fmt),
                    usedValues,
                    rounded
            ));
        }

        // —— 4) 可选：稳定排序（按监测点ID→采样时间） —— //
        // 若 CalcResultRow 有 getter，请按需要启用；也可在构造前对 groupedData 排序
        // resultRows.sort(Comparator.comparing(CalcResultRow::getPointId).thenComparing(CalcResultRow::getSampleDate));

        return resultRows;
    }

// —— 工具方法 —— //

    // 从公式中“按词边界”提取变量名（候选集不为空时，仅在候选集中匹配）
    private static Set<String> extractUsedAttributes(String formula, Set<String> candidates) {
        Set<String> out = new LinkedHashSet<>();
        for (String a : candidates) {
            if (Pattern.compile("\\b" + Pattern.quote(a) + "\\b").matcher(formula).find()) {
                out.add(a);
            }
        }
        return out;
    }

    private static double round2(double v) {
        return new BigDecimal(v).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private static IllegalArgumentException badRequest(String msg) {
        return new IllegalArgumentException(msg);
    }

    private static Integer castInt(Object o) {
        if (o == null) return null;
        if (o instanceof Integer i) return i;
        if (o instanceof Number n) return n.intValue();
        return Integer.valueOf(o.toString());
    }

    private static Double castDouble(Object o) {
        if (o == null) return null;
        if (o instanceof Double d) return d;
        if (o instanceof Number n) return n.doubleValue();
        return Double.valueOf(o.toString());
    }

    private static LocalDateTime castDateTime(Object o) {
        if (o == null) return null;
        if (o instanceof LocalDateTime t) return t;
        if (o instanceof java.sql.Timestamp ts) return ts.toLocalDateTime();
        String s = o.toString();
        // 兼容 'yyyy-MM-dd HH:mm:ss' → 'yyyy-MM-ddTHH:mm:ss'
        if (s.indexOf('T') < 0 && s.indexOf(' ') >= 0) s = s.replace(' ', 'T');
        return LocalDateTime.parse(s);
    }

    // 你原来的 Key；若用 Lombok @Data，请保留；否则请实现 equals/hashCode
    @lombok.Data
    public static class Key {
        private final Integer mpId;
        private final LocalDateTime samplingDate;
    }

    // —— 受支持变量（备选）：当 getAttributeDict() 为空时会使用此集合 —— //
    private static final Set<String> SUPPORTED_ATTRS = Set.of(
            "PH",
            "CEC",
            "Alkali_Hydrolyzable_Nitrogen",
            "Nitrate_Nitrogen",
            "Available_Phosphorus",
            "Available_Potassium",
            "Slowly_Available_Potassium",
            "Organic_Matter",
            "Exchangeable_Calcium",
            "Exchangeable_Magnesium",
            "Available_Sulfur",
            "Water_Soluble_Chlorine",
            "Available_Copper",
            "Available_Iron",
            "Available_Manganese",
            "Available_Boron",
            "Available_Zinc",
            "Available_Molybdenum",
            "Available_Silicon",
            "Total_Nitrogen",
            "Total_Phosphorus",
            "Total_Potassium",
            "Cultivated_Layer_Thickness",
            "Bulk_Density",
            "Soil_Compaction",
            "Aggregate_Structure",
            "Microbial_Biomass_Carbon",
            "Soil_Salinity",
            "Mulch_Residue",
            "Rapidly_Available_Potassium",
            "Available_Phosphorus_NH4F_HCl",
            "Available_Phosphorus_NaHCO3"
    );


    // 可选：若你希望 Service 直接提供“代码→中文名”字典，重写这个方法
    @Override
    public Map<String, String> getAttributeDict() {
        try {
            // 拿到完整的枚举 + 中文翻译
            EnumDictDTO dto = enumDictService.getEnumDict(false);
            if (dto == null || dto.getI18n() == null) {
                return Map.of();
            }
            Map<String, Map<String, String>> i18n = dto.getI18n();

            // 只要 SoilAttribute 这一块：key 就是 PH / Nitrate_Nitrogen 这种代码
            Map<String, String> soilAttrs = i18n.get("SoilAttribute");
            if (soilAttrs == null || soilAttrs.isEmpty()) {
                return Map.of();
            }

            return soilAttrs;
        } catch (Exception e) {
            // 一旦字典出问题，不要拖垮计算功能，直接退回空 Map，让 SUPPORTED_ATTRS 顶上
            return Map.of();
        }
    }
    private static Object getFieldIgnoreCase(Map<String, Object> row, String key) {
        if (row == null || key == null) return null;

        // 1) 先按原 key 取一遍（以防 mapper 恰好就是这个大小写）
        Object v = row.get(key);
        if (v != null) return v;

        // 2) 再做一遍忽略大小写匹配
        for (Map.Entry<String, Object> e : row.entrySet()) {
            if (e.getKey() != null && e.getKey().equalsIgnoreCase(key)) {
                return e.getValue();
            }
        }
        return null;
    }


}
