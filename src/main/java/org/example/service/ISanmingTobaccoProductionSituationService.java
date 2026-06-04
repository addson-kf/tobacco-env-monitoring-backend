package org.example.service;

import org.example.dto.SanmingTobaccoProductionSituationDTO.SanmingTobaccoProductionSituationDTO;
import org.example.response.common.Page.PageResult;
import org.example.model.SanmingTobaccoProductionSituation;

import java.util.List;

public interface ISanmingTobaccoProductionSituationService {
    SanmingTobaccoProductionSituationDTO findOne(Integer year, String county, String town, String village);

    List<SanmingTobaccoProductionSituationDTO> list(Integer year, String county, String town, String village);

    PageResult<SanmingTobaccoProductionSituationDTO> page(Integer year, String county, String town, String village, int page, int size);

    void update(SanmingTobaccoProductionSituation dto);

    void add(SanmingTobaccoProductionSituation dto);

    void delete(Integer year, String countyDistrict, String town, String village);
}
