package org.example.controller;

import org.example.dto.EnumDictDTO.EnumDictDTO;
import org.example.service.IEnumDictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/enums")
public class EnumDictController {

    @Autowired
    private IEnumDictService enumDictService;
    public EnumDictController(IEnumDictService enumDictService) { this.enumDictService = enumDictService; }

    // 前端手动刷新枚举字典（默认自动热加载）
    @GetMapping
    public EnumDictDTO getEnumDict(@RequestParam(defaultValue = "true") boolean reloadIfChanged) {
        return enumDictService.getEnumDict(reloadIfChanged);
    }

    // 管理员在线更新外部 JSON（可选，推荐加权限）
    @PutMapping
    public String updateEnumDict(@RequestBody Map<String, Map<String, String>> newDict) {
        enumDictService.updateDict(newDict);
        return "ok";
    }

}
