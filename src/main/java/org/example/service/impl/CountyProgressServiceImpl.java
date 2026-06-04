package org.example.service.impl;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.example.mapper.CountyProgressMapper; // 如果你用 org.example.mapper，请改成 mapper 并保证 @MapperScan
import org.example.dto.CountyProgressDTO.CountyProgressDTO;
import org.example.dto.CountyProgressDTO.CountyProgressUpsertRequest;
import org.example.model.CountyProgress;
import org.example.response.common.Page.PageResult;
import org.example.service.ICountyProgressService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service

public class CountyProgressServiceImpl implements ICountyProgressService {

    private final CountyProgressMapper mapper;

    private final ObjectMapper objectMapper;

    // ① 一定要注入这个 Resource（别忘了 import org.springframework.core.io.Resource）
    @Value("${progress.rules-path:classpath:progress/progress-rules.json}")
    private Resource rulesResource;

    public CountyProgressServiceImpl(CountyProgressMapper mapper, ObjectMapper objectMapper) {
        this.mapper = mapper;
        this.objectMapper = objectMapper;
    }
     @Override
     public void upsert(CountyProgressUpsertRequest req) {
         CountyProgress cp = new CountyProgress();
         cp.setCountyDistrict(req.getCountyDistrict());
         cp.setTown(req.getTown());
         cp.setVillage(req.getVillage());
         cp.setProgress(req.getProgress());
         cp.setMonitoringPointID(req.getMonitoringPointID());
         mapper.upsert(cp);
     }

    @Override
    public void updateById(CountyProgressDTO dto) {
        CountyProgress cp = new CountyProgress();
        cp.setCountyId(dto.getCountyId());
        cp.setCountyDistrict(dto.getCountyDistrict());
        cp.setTown(dto.getTown());
        cp.setVillage(dto.getVillage());
        cp.setProgress(dto.getProgress());
        cp.setMonitoringPointID(dto.getMonitoringPointID());
        mapper.updateById(cp);
    }

    @Override
    public CountyProgressDTO findOne(String county, String town, String village) {
        return toDTO(mapper.findOne(county, town, village));
    }

    @Override
    public List<CountyProgressDTO> list(String county, String town, String village) {
        return mapper.list(county, town, village)
                .stream().map(this::toDTO).collect(Collectors.toList());
    }

    @Override
    public PageResult<CountyProgressDTO> page(String county, String town, String village, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.max(size, 1);
        int offset = (safePage - 1) * safeSize;

        long total = mapper.countByFilter(county, town, village);
        List<CountyProgressDTO> items = mapper.pageByFilter(county, town, village, offset, safeSize)
                .stream().map(this::toDTO).collect(Collectors.toList());

        PageResult<CountyProgressDTO> pr = new PageResult<>();
        pr.setItems(items);
        pr.setTotal((int) total);
        pr.setPage(safePage);
        pr.setSize(safeSize);
        pr.setPages((int) Math.ceil(total * 1.0 / safeSize));
        pr.setHasNext(safePage * safeSize < total);
        return pr;
    }

    private CountyProgressDTO toDTO(CountyProgress cp) {
        if (cp == null) return null;
        CountyProgressDTO d = new CountyProgressDTO();
        d.setCountyId(cp.getCountyId());
        d.setCountyDistrict(cp.getCountyDistrict());
        d.setTown(cp.getTown());
        d.setVillage(cp.getVillage());
        d.setProgress(cp.getProgress());
        d.setMonitoringPointID(cp.getMonitoringPointID());
        return d;
    }


    @Override
    @Transactional
    public void recomputeProgress(String county, String town, Integer year, String stage) {
        // ③ 读 JSON（放在方法里，别在字段上读）
        JsonNode root = readRulesJson();

        // ④ 解析阶段 → months（允许 stage 为空；也允许没有 months 节点）
        List<Integer> months = null;
        if (stage != null && !stage.isBlank()) {
            JsonNode stagesNode = root.path("stages");
            JsonNode stageNode  = stagesNode.path(stage);
            JsonNode monthsNode = stageNode.path("months");
            if (monthsNode.isArray() && monthsNode.size() > 0) {
                months = new ArrayList<>(monthsNode.size());
                for (JsonNode n : monthsNode) {
                    months.add(n.asInt());
                }
            }
            // 如果以后你支持 dateRanges，这里也可以先解析好传给 mapper（当前示例只演示 months）
        }

        // ⑤ 读取权重/阈值
        double wSoil    = getWeight(root, "soil");
        double wEnzyme  = getWeight(root, "enzyme");
        double wLeaf    = getWeight(root, "leaf");
        double wSmoke   = getWeight(root, "smoke");
        double wWeather = getWeight(root, "weather");
        int    weatherReq  = getRequired(root, "weather");
        String smokeMpCol  = getMpIdColumn(root, "smoke", "MonitoringPointID"); // 或 "MonitoringPointId"

        // ⑥ 调用 mapper：注意签名要和你的 XML/接口一致
        // 如果你的 XML 是我给的“带 stage 版本”，请用下面这一行（多一个 stage 参数）：
        // mapper.recomputeProgress(county, town, year, stage, months, weatherReq, wSoil, wEnzyme, wLeaf, wSmoke, wWeather, smokeMpCol);

        // 如果你的 XML 还是“老版本（不含 stage 参数）”，请用这一行：
        mapper.recomputeProgress(county, town, year, months, weatherReq, wSoil, wEnzyme, wLeaf, wSmoke, wWeather, smokeMpCol);
    }

    /** 读取 JSON 规则（带 null/IO 保护） */
    private JsonNode readRulesJson() {
        if (rulesResource == null) {
            throw new IllegalStateException("progress-rules.json 未注入，请确认路径：src/main/resources/progress/progress-rules.json 或配置 progress.rules-path");
        }
        try (InputStream in = rulesResource.getInputStream()) {
            return objectMapper.readTree(in);
        } catch (IOException e) {
            throw new RuntimeException("读取 progress-rules.json 失败: " + rulesResource, e);
        }
    }

    private double getWeight(JsonNode root, String key) {
        for (JsonNode ds : root.path("datasets")) {
            if (key.equals(ds.path("key").asText())) return ds.path("weight").asDouble(0.0);
        }
        return 0.0;
    }

    private int getRequired(JsonNode root, String key) {
        for (JsonNode ds : root.path("datasets")) {
            if (key.equals(ds.path("key").asText())) return ds.path("required").asInt(1);
        }
        return 1;
    }

    private String getMpIdColumn(JsonNode root, String key, String defVal) {
        for (JsonNode ds : root.path("datasets")) {
            if (key.equals(ds.path("key").asText())) {
                return ds.has("monitoringPointIdColumn") ? ds.get("monitoringPointIdColumn").asText() : defVal;
            }
        }
        return defVal;
    }





}
