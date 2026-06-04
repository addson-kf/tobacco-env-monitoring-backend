package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.dto.calc.CalcRequest;
import org.example.dto.calc.CalcResultRow;
import org.example.response.common.calc.CalcResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface ICalculationService {
    List<CalcResultRow> calculate(CalcRequest req);
    default Map<String,String> getAttributeDict() { return Map.of(); }


}
