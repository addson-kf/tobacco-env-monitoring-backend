package org.example.service;

import org.example.dto.analysis.MaxminDTO;

public interface IMaxminService {

    MaxminDTO soilExtrema(String attr, Integer year, String periodZh,
                          String countyDistrict, String town, String village,String tobaccoFieldType,
                          Boolean useEnum);

    MaxminDTO enzymeExtrema(String attr, Integer year, String periodZh,
                            String countyDistrict, String town, String village,
                            Boolean useEnum);

    MaxminDTO leafExtrema(String attr, Integer year, String periodZh,
                          String countyDistrict, String town, String village,
                          Boolean useEnum);

    MaxminDTO weatherExtrema(String attr, Integer year, String periodZh,
                             String countyDistrict, String town, String village,
                             Boolean useEnum);
}
