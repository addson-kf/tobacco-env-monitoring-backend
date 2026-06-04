package org.example.service.impl;

import org.example.dto.analysis.CorrelationResultDTO;
import org.example.dto.analysis.MaxminDTO;
import org.example.dto.analysis.MeanResultDTO;
import org.example.mapper.AnalysisMapper;
import org.example.mapper.SoilDataCalcMapper;
import org.example.model.analysis.CorrelationXY;
import org.example.service.IAnalysisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.example.service.IEnumDictService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnalysisServiceImpl implements IAnalysisService {


    private final AnalysisMapper analysisMapper;
    private final IEnumDictService enumDictService;
    private final SoilDataCalcMapper soilDataCalcMapper;


    public AnalysisServiceImpl(AnalysisMapper analysisMapper,
                               IEnumDictService enumDictService,
                               SoilDataCalcMapper soilDataCalcMapper) { // ✅ 多一个参数
        this.analysisMapper = analysisMapper;
        this.enumDictService = enumDictService;
        this.soilDataCalcMapper = soilDataCalcMapper;      // ✅ 赋值
    }
    @Value("${analysis.default-year:2025}")
    private Integer defaultYear;


    private String normalizePeriod(String input) {
        if (input == null) return null;
        String key = input.trim();
        return PERIOD_ALIASES.getOrDefault(key, key);
    }

    /** 将输入（可能是中文/英文别写法）统一成“字典里的标准英文常量名” */
    private String normalizeByGroup(String group, String incoming, Map<String,String> aliasFallback) {
        if (incoming == null) return null;
        String t = incoming.trim();
        // 取字典该组
        var all = enumDictService.getEnumDict(false);
        Map<String, Map<String, String>> i18n = (all == null) ? null : all.getI18n();
        Map<String,String> dictGroup = (i18n == null) ? null : i18n.get(group);

        // 1) 已是标准英文键
        if (dictGroup != null && dictGroup.containsKey(t)) return t;

        // 2) 中文 -> 反查英文键
        if (dictGroup != null) {
            for (Map.Entry<String,String> e : dictGroup.entrySet()) {
                if (t.equals(e.getValue())) return e.getKey();
            }
        }

        // 3) 别名表
        if (aliasFallback != null) {
            String m = aliasFallback.get(t);
            if (m != null) return m;
        }

        // 4) 宽松匹配：去空格/_/-，不区分大小写
        String canon = t.replaceAll("[_\\-\\s]", "").toLowerCase(Locale.ROOT);
        if (dictGroup != null) {
            for (String k : dictGroup.keySet()) {
                String ck = k.replaceAll("[_\\-\\s]", "").toLowerCase(Locale.ROOT);
                if (ck.equals(canon)) return k;
            }
        }
        return t; // 找不到就原样返回（前提：Mapper 不使用 ::enum 强转）
    }

    // 土壤数据指标：
    private static final Map<String, String> SOIL_ALIAS = Map.ofEntries(
            Map.entry("ph", "PH"),
            Map.entry("pH", "PH"),
            Map.entry("PH", "PH"),
            Map.entry("阳离子交换量", "CEC"),
            Map.entry("碱解氮", "Alkali_Hydrolyzable_Nitrogen"),
            Map.entry("硝态氮", "Nitrate_Nitrogen"),
            Map.entry("有效磷", "Available_Phosphorus"),
            Map.entry("有效钾", "Available_Potassium"),
            Map.entry("有机碳", "Organic_Matter"),
            Map.entry("有机质", "Organic_Matter"),
            Map.entry("交换性钙", "Exchangeable_Calcium"),
            Map.entry("交换性镁", "Exchangeable_Magnesium"),
            Map.entry("有效硫", "Available_Sulfur"),
            Map.entry("水溶性氯", "Water_Soluble_Chlorine"),
            Map.entry("有效铜", "Available_Copper"),
            Map.entry("有效铁", "Available_Iron"),
            Map.entry("有效锰", "Available_Manganese"),
            Map.entry("有效硼", "Available_Boron"),
            Map.entry("缓效钾", "Slowly_Available_Potassium"),
            Map.entry("有效锌", "Available_Zinc"),
            Map.entry("有效钼", "Available_Molybdenum"),
            Map.entry("全氮", "Total_Nitrogen"),
            Map.entry("全磷", "Total_Phosphorus"),
            Map.entry("全钾", "Total_Potassium"),
            Map.entry("耕层厚度", "Cultivated_Layer_Thickness"),
            Map.entry("土壤容重", "Bulk_Density"),
            Map.entry("土壤紧实度", "Soil_Compaction"),
            Map.entry("水稳性大团聚体", "Aggregate_Structure"),
            Map.entry("土壤微生物生物量碳", "Microbial_Biomass_Carbon"),
            Map.entry("土壤盐xty", "Soil_Salinity"),
            Map.entry("农膜残留量", "Mulch_Residue"),
            Map.entry("速效钾", "Rapidly_Available_Potassium"),
            Map.entry("有效磷-氟化铵盐酸", "Available_Phosphorus_NH4F_HCl"),
            Map.entry("有效磷-碳酸氢钠提取法", "Available_Phosphorus_NaHCO3")

    );
    // 叶片指标常见别名
    private static final Map<String, String> LEAF_ALIAS = new HashMap<String, String>() {{
        put("尼古丁", "Nicotine");
        put("总糖", "Total_Sugar");
        put("还原糖", "ReducingSugar");
        put("总氮", "TotalNitrogen");
        put("总磷", "TotalPhosphorus");
        put("总钾", "TotalPotassium");
        put("总氯", "TotalChlorine");
        put("糖氮比", "SugarNitrogenRatio");
        put("糖尼古丁比", "SugarNicotineRatio");
        put("水溶性糖蛋白比", "WaterSolubleSugarProteinRatio");
        put("还原糖总糖比", "ReducingSugarTotalSugarRatio");
        put("钾氯比", "PotassiumChlorineRatio");
        put("钙", "Calcium");
        put("镁", "Magnesium");
        put("硫", "Sulfur");
        put("铁", "Iron");
        put("锰", "Manganese");
    }};

    // 天气指标（你的 JSON 用下划线小写）
    private static final Map<String,String> WEATHER_ALIAS = new HashMap<String,String>() {{
        put("土壤温度", "soil_temperature");
        put("土壤湿度", "soil_moisture");
        put("空气温度", "air_temperature");
        put("空气湿度", "air_humidity");
        put("风速", "wind_speed");
        put("风向", "wind_direction");
        put("降水量", "precipitation");
        put("太阳辐射", "solar_radiation");
    }};

    // 土壤酶活性指标（SoilEnzymeActivityAnalysis）
    private static final Map<String,String> SOIL_ENZYME_ALIAS = new HashMap<String,String>() {{
        put("土壤中的酸性磷酸酶活性", "AcidPhosphatase");
        put("土壤中的酸性蛋白酶活性", "AcidProtease");
        put("土壤中的尿素酶活性", "Urease");
        put("土壤中的硝酸还原酶活性", "NitrateReductase");
        put("土壤中的微生物群落结构分析","MicrobialCommunityStructure");
        put("土壤中的微生物组成分析", "MicrobialCompositionAnalysis");
        put("土壤中的物种区分分析", "SpeciesDifferentiationAnalysis");
        put("土壤中的微生物关联聚类分析", "MicrobialCorrelationClustering");
        put("16S基因分析中的功能预测", "FunctionalPrediction16S");
    }};

    // 感官评价（TobaccoSmokingEvaluation）
    private static final Map<String,String> SMOKING_ALIAS = new HashMap<String,String>() {{
        put("劲头", "Strength");
        put("烟雾质量", "SmokeQuality");
        put("异味", "OffFlavor");
        put("刺激性", "Irritation");          // 你 JSON 里 "Irritation" 有个前导空格，记得修掉
        put("风味特征", "StyleCharacteristics");
    }};

    private String normalizeAttr(String dataset, String input) {
        if (input == null) return null;
        String t = input.trim();
        String ds = (dataset == null ? "" : dataset.trim().toLowerCase(Locale.ROOT));

        Map<String, String> alias;
        switch (ds) {
            case "soil":
            case "soildatas":
                alias = SOIL_ALIAS;
                break;
            case "enzyme":
            case "soilenzyme":
            case "soilenzymeactivitydatas":
                alias = SOIL_ENZYME_ALIAS;
                break;
            case "leaf":
            case "tobaccoleaf":
            case "tobaccoleafdatas":
                alias = LEAF_ALIAS;
                break;
            case "weather":
            case "weatherdatas":
                alias = WEATHER_ALIAS;
                break;
            case "smoking":
            case "smokingevaluation":
            case "smokingevaluationdatas":
                alias = SMOKING_ALIAS;
                break;
            default:
                alias = null;
        }

        if (alias != null && alias.containsKey(t)) {
            return alias.get(t);
        }
        return t;  // 找不到就直接用原值（前端直接传英文就会走这里）
    }

    // -------- 阶段归一化（中文 -> 英文，与数据库存储保持一致） --------
    private static final Map<String, String> PERIOD_ALIASES = new HashMap<String, String>() {{
        put("种烟前", "Before planting tobacco");
        put("种烟后", "After planting tobacco");
        put("种稻后", "After harvesting rice");
    }};

    /** dataset -> 字典组名 */
    private String groupNameFor(String dataset) {
        String ds = dataset == null ? "" : dataset.trim().toLowerCase(Locale.ROOT);
        switch (ds) {
            case "soil":
            case "soildatas":
                return "SoilAttribute";
            case "enzyme":
            case "soilenzyme":
            case "soilenzymeactivitydatas":
                return "SoilEnzymeActivityAnalysis";
            case "leaf":
            case "tobaccoleaf":
            case "tobaccoleafdatas":
                return "TobaccoLeafDataEnum";
            case "weather":
            case "weatherdatas":
                return "WeatherType";
            case "smoking": case "smokingevaluation":
            case "smokingevaluationdatas":
                return "TobaccoSmokingEvaluation"; // ← 新增
            default:
                return null;
        }
    }




    @Override
    public CorrelationResultDTO correlation(String dataset,
                                            String attrX,
                                            String attrY,
                                            Integer year,
                                            String periodZh,
                                            String countyDistrict,
                                            String town,
                                            String village) {

        final String periodForSql = normalizePeriod(periodZh); // 中文→英文阶段
        final String group = groupNameFor(dataset); // 获取数据集对应的 group 名称

        if (group == null) {
            return new CorrelationResultDTO(); // 如果没有匹配的 group，返回空结果
        }

        // 根据 group 返回对应的 alias 映射
        Map<String, String> alias = null;
        switch (group) {
            case "SoilAttribute":
                alias = SOIL_ALIAS;
                break;
            case "TobaccoLeafDataEnum":
                alias = LEAF_ALIAS;
                break;
            case "WeatherType":
                alias = WEATHER_ALIAS;
                break;
            case "SoilEnzymeActivityAnalysis":
                alias = SOIL_ENZYME_ALIAS;
                break;
            case "TobaccoSmokingEvaluation":
                alias = SMOKING_ALIAS;
                break;
            default:
                alias = null;
                break;
        }

        // 归一化处理属性名称
        final String ax = normalizeByGroup(group, attrX, alias);
        final String ay = normalizeByGroup(group, attrY, alias);

        List<CorrelationXY> pairs;
        switch (dataset.toLowerCase(Locale.ROOT)) {
            case "soil":
            case "soildatas":
                pairs = analysisMapper.soilPairs(ax, ay, year, periodForSql, countyDistrict, town, village);
                break;
            case "enzyme":
            case "soilenzyme":
                pairs = analysisMapper.enzymePairs(ax, ay, year, periodForSql, countyDistrict, town, village);
                break;
            case "leaf":
            case "tobaccoleaf":
                pairs = analysisMapper.leafPairs(ax, ay, year, periodForSql, countyDistrict, town, village);
                break;
            case "weather":
            case "weatherdatas":
                pairs = analysisMapper.weatherPairs(ax, ay, year, periodForSql, countyDistrict, town, village);
                break;
            case "smoking":
                pairs = analysisMapper.smokingPairs(ax, ay, year, periodForSql, countyDistrict, town, village);
                break;
            default:
                pairs = Collections.emptyList();
        }

        int n = pairs.size();
        double meanX = 0.0, meanY = 0.0, r = 0.0;
        if (n > 0) {
            meanX = pairs.stream().mapToDouble(CorrelationXY::getX).average().orElse(0.0);
            meanY = pairs.stream().mapToDouble(CorrelationXY::getY).average().orElse(0.0);
        }

        if (n > 1) {
            double sumX = 0, sumY = 0, sumX2 = 0, sumY2 = 0, sumXY = 0;
            for (CorrelationXY p : pairs) {
                double x = p.getX();
                double y = p.getY();
                sumX += x;
                sumY += y;
                sumX2 += x * x;
                sumY2 += y * y;
                sumXY += x * y;
            }
            double num = n * sumXY - sumX * sumY;
            double den = Math.sqrt((n * sumX2 - sumX * sumX) * (n * sumY2 - sumY * sumY));
            r = (den == 0) ? 0.0 : (num / den);
        }

        CorrelationResultDTO dto = new CorrelationResultDTO();
        dto.setDataset(dataset);
        dto.setAttrX(attrX);
        dto.setAttrY(attrY);
        dto.setYear(year);
        dto.setPeriod(periodZh); // DTO 展示中文阶段
        dto.setCountyDistrict(countyDistrict);
        dto.setTown(town);
        dto.setVillage(village);
        dto.setN(n);
        dto.setMeanX(meanX);
        dto.setMeanY(meanY);
        dto.setR(r);
        dto.setPairs(pairs);
        return dto;
    }

    // ====== 平均值 ======
    @Override
    public MeanResultDTO mean(String dataset,
                              String attr,
                              Integer year,
                              String period,
                              String countyDistrict,
                              String town,
                              String village,
                              String tobaccoFieldType) {

        if (year == null) {
            year = null; // 不传=全量
        } // 不传年份=全量；若想走默认年，改成: year = defaultYear;
        String periodForSql = normalizePeriod(period);
        String normAttr = normalizeAttr(dataset, attr);


        Map<String, Object> row;
        switch ((dataset == null ? "" : dataset.trim()).toLowerCase(Locale.ROOT)) {
            case "soil":
            case "soildatas":
                row = analysisMapper.meanSoil(normAttr, year, periodForSql, countyDistrict, town, village, tobaccoFieldType);
                break;
            case "enzyme":
            case "soilenzyme":
            case "soilenzymeactivitydatas":
                row = analysisMapper.meanEnzyme(normAttr, year, periodForSql, countyDistrict, town, tobaccoFieldType);
                break;
            case "leaf":
            case "tobaccoleaf":
            case "tobaccoleafdatas":
                row = analysisMapper.meanLeaf(normAttr, year, periodForSql, countyDistrict, town, tobaccoFieldType);
                break;
            case "weather":
            case "weatherdatas":
                row = analysisMapper.meanWeather(normAttr, year, periodForSql, countyDistrict, town, village);
                break;
            case "smoking":
            case "smokingevaluation":
            case "smokingevaluationdatas":
                row = analysisMapper.meanSmoking(normAttr, year, periodForSql, countyDistrict, town, village);
                break;
            default:
                throw new IllegalArgumentException("不支持的数据集类型: " + dataset);
        }

        int n = 0;
        double mean = 0.0;
        if (row != null) {
            Object nObj = row.get("n");
            Object meanObj = row.get("mean");
            if (nObj != null) n = ((Number) nObj).intValue();
            if (meanObj != null) mean = ((Number) meanObj).doubleValue();
        }

        MeanResultDTO dto = new MeanResultDTO();
        dto.setDataset(dataset);
        dto.setAttr(attr);
        dto.setYear(year);
        dto.setPeriod(period); // 回显原始入参
        dto.setCountyDistrict(countyDistrict);
        dto.setTown(town);
        dto.setVillage(village);
        dto.setN(n);
        dto.setMean(mean);
        return dto;
    }

    @Override
    public MaxminDTO soilMaxmin(String attr,
                                String tobaccoFieldType,
                                String countyDistrict,
                                String town,
                                String village) {

        // 用你已有的 SoilDataCalcMapper 查询这个属性的所有值
        var rows = soilDataCalcMapper.queryForCalc(
                tobaccoFieldType,
                countyDistrict,
                town,
                village,
                null,  // samplingDateFrom
                null,  // samplingDateTo
                java.util.List.of(attr)
        );

        Double max = null;
        Double min = null;

        for (java.util.Map<String, Object> row : rows) {
            Object vObj = row.get("value"); // 别名在 SoilDataCalcMapper.xml 里一般叫 value
            if (vObj == null) continue;
            if (!(vObj instanceof Number)) continue;

            double v = ((Number) vObj).doubleValue();
            if (max == null || v > max) max = v;
            if (min == null || v < min) min = v;
        }

        MaxminDTO dto = new MaxminDTO();
        dto.setMax(max);
        dto.setMin(min);
        return dto;
    }


}
