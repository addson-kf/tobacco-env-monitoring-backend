package org.example.service.impl;

import org.example.dto.analysis.MaxminDTO;
import org.example.mapper.MaxminMapper;
import org.example.service.IMaxminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IMaxminServiceImpl implements IMaxminService {
    private final MaxminMapper mapper;

    // 这是构造函数：没有返回值（连 void 都没有），名字和类名完全一致
    @Autowired
    public IMaxminServiceImpl(MaxminMapper mapper) {
        this.mapper = mapper;
    }
    @Override
    public MaxminDTO soilExtrema(String attr, Integer year, String periodZh,
                                 String countyDistrict, String town, String village, String tobaccoFieldType,
                                 Boolean useEnum) {
        Double max = mapper.soilMax(attr, year, periodZh, countyDistrict, town, village, tobaccoFieldType,useEnum);
        Double min = mapper.soilMin(attr, year, periodZh, countyDistrict, town, village, tobaccoFieldType,useEnum);
        return new MaxminDTO(max, min);
    }

    @Override
    public MaxminDTO enzymeExtrema(String attr, Integer year, String periodZh,
                                   String countyDistrict, String town, String village,
                                   Boolean useEnum) {
        Double max = mapper.enzymeMax(attr, year, periodZh, countyDistrict, town, village, useEnum);
        Double min = mapper.enzymeMin(attr, year, periodZh, countyDistrict, town, village, useEnum);
        return new MaxminDTO(max, min);
    }

    @Override
    public MaxminDTO leafExtrema(String attr, Integer year, String periodZh,
                                 String countyDistrict, String town, String village,
                                 Boolean useEnum) {
        Double max = mapper.leafMax(attr, year, periodZh, countyDistrict, town, village, useEnum);
        Double min = mapper.leafMin(attr, year, periodZh, countyDistrict, town, village, useEnum);
        return new MaxminDTO(max, min);
    }

    @Override
    public MaxminDTO weatherExtrema(String attr, Integer year, String periodZh,
                                    String countyDistrict, String town, String village,
                                    Boolean useEnum) {
        Double max = mapper.weatherMax(attr, year, periodZh, countyDistrict, town, village, useEnum);
        Double min = mapper.weatherMin(attr, year, periodZh, countyDistrict, town, village, useEnum);
        return new MaxminDTO(max, min);
    }
}
