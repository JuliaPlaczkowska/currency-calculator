package pl.example.currencycalculator.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.example.currencycalculator.service.ExchangeService;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/currencies")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(exchangeService.getAllCodes());
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convert(@RequestParam String codeAsk,
                          @RequestParam float valueAsk,
                          @RequestParam String codeBid) {
        String conversionResult = exchangeService
                .convertCurrency(
                        codeAsk,
                        valueAsk,
                        codeBid
                );
        return ResponseEntity.ok(conversionResult);
    }

    @GetMapping("/rates")
    public ResponseEntity<?> getRatesForCurrencySet(@RequestBody Set<String> codes) {
        return ResponseEntity.ok(exchangeService.getByCodes(codes));
    }
}
