package org.example.service.impl;

import org.example.dto.SanmingTobaccoProductionSituationDTO.SanmingTobaccoProductionSituationDTO;
import org.example.mapper.SanmingTobaccoProductionSituationMapper;
import org.example.model.SanmingTobaccoProductionSituation;
import org.example.response.common.Page.PageResult;
import org.example.service.ISanmingTobaccoProductionSituationService;
import org.springframework.stereotype.Service;
import org.example.model.SanmingTobaccoProductionSituation;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SanmingTobaccoProductionSituationServiceImpl implements ISanmingTobaccoProductionSituationService{
    private final SanmingTobaccoProductionSituationMapper mapper;

    public SanmingTobaccoProductionSituationServiceImpl(SanmingTobaccoProductionSituationMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public SanmingTobaccoProductionSituationDTO findOne(Integer year, String county, String town, String village) {
        SanmingTobaccoProductionSituation m = mapper.findOne(year, county, town, village);
        return toDto(m);
    }

    @Override
    public List<SanmingTobaccoProductionSituationDTO> list(Integer year, String county, String town, String village) {
        return mapper.list(year, county, town, village)
                .stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public PageResult<SanmingTobaccoProductionSituationDTO> page(Integer year, String county, String town, String village, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 200);
        int offset = (safePage - 1) * safeSize;

        long total = mapper.countByFilter(year, county, town, village);
        List<SanmingTobaccoProductionSituationDTO> items = mapper.pageByFilter(year, county, town, village, offset, safeSize)
                .stream().map(this::toDto).collect(Collectors.toList());

        return PageResult.of(items, total, safePage, safeSize);
    }

    private SanmingTobaccoProductionSituationDTO toDto(SanmingTobaccoProductionSituation m) {
        if (m == null) return null;
        SanmingTobaccoProductionSituationDTO d = new SanmingTobaccoProductionSituationDTO();
        d.setCountyDistrict(m.getCountyDistrict());
        d.setTown(m.getTown());
        d.setVillage(m.getVillage());

        d.setArea(m.getArea());
        d.setContractQuantity(m.getContractQuantity());
        d.setAcquisitionVolume(m.getAcquisitionVolume());

        d.setTobaccoFarming(m.getTobaccoFarming());
        d.setTobaccoTechnic(m.getTobaccoTechnic());
        d.setRoastingRooms(m.getRoastingRooms());
        d.setSeedlingShelter(m.getSeedlingShelter());

        d.setRemarks(m.getRemarks());
        d.setYear(m.getYear());
        return d;
    }

    @Override
    public void update(SanmingTobaccoProductionSituation dto) {
        mapper.update(dto);
    }
    @Override
    public void add(SanmingTobaccoProductionSituation dto) {
        mapper.add(dto);
    }

    @Override
    public void delete(Integer year, String countyDistrict, String town, String village) {
        mapper.delete(year, countyDistrict, town, village);
    }
}
