package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.dto.calc.CalcRequest;
import org.example.dto.calc.CalcResultRow;
import org.example.response.common.calc.CalcResponse;
import org.example.service.ICalculationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.calculation.base-path}")
public class CalculationController {

    private final ICalculationService calculationService;

    @PostMapping("${api.calculation.endpoints.execute}")
    public ResponseEntity<CalcResponse> execute(@RequestBody CalcRequest req) {
        List<CalcResultRow> rows = calculationService.calculate(req);
        // 直接用你已有的静态工厂
        return ResponseEntity.ok(CalcResponse.ok(rows));
    }

    // 把业务层抛出的非法参数映射为 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CalcResponse> handleIllegalArgument(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(CalcResponse.fail(e.getMessage()));
    }

}
