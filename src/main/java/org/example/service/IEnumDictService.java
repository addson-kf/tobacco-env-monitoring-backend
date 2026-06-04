package org.example.service;

import org.example.dto.EnumDictDTO.EnumDictDTO;
import java.util.Map;

public interface IEnumDictService {

    // reloadIfChanged = true 时，如果外部 JSON 文件被修改过会自动重载
    EnumDictDTO getEnumDict(boolean reloadIfChanged);

    // （可选）在线更新：把新字典写回 externalPath
    void updateDict(Map<String, Map<String, String>> newDict);

}
