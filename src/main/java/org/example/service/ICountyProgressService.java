package org.example.service;

import org.example.dto.CountyProgressDTO.CountyProgressDTO;
import org.example.dto.CountyProgressDTO.CountyProgressUpsertRequest;
import org.example.model.CountyProgress;
import org.example.response.common.Page.PageResult;

import java.util.List;

public interface ICountyProgressService {
    void upsert(CountyProgressUpsertRequest req);

    void updateById(CountyProgressDTO dto);

    CountyProgressDTO findOne(String county, String town, String village);

    List<CountyProgressDTO> list(String county, String town, String village);

    PageResult<CountyProgressDTO> page(String county, String town, String village, int page, int size);

    void recomputeProgress(String county, String town, Integer year, String stage);
}
