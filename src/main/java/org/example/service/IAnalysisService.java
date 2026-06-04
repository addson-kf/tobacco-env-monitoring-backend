package org.example.service;

import org.example.dto.analysis.CorrelationResultDTO;
import org.example.dto.analysis.MaxminDTO;
import org.example.dto.analysis.MeanResultDTO;

public interface IAnalysisService {
    CorrelationResultDTO correlation(
                                     String dataset,
                                     String attrX,
                                     String attrY,
                                     Integer year,
                                     String periodZh,     // 种烟前/种烟后/种稻后 / null
                                     String countyDistrict,
                                     String town,
                                     String village);

    MeanResultDTO mean(String dataset,
                       String attr,
                       Integer year,
                       String periodZh,        // 前端传中文亦可
                       String countyDistrict,
                       String town,
                       String village,
                       String tobaccoFieldType);

    MaxminDTO soilMaxmin(String attr,
                         String tobaccoFieldType,
                         String countyDistrict,
                         String town,
                         String village);
}

