package pl.example.currencycalculator.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.example.currencycalculator.service.ExchangeService;

@AllArgsConstructor
@RestController
public class ExchangeController {

    private final ExchangeService exchangeService;

    @GetMapping("/currencies")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(exchangeService.getAllCodes());
    }

    @GetMapping("/convert")
    public String convert(@RequestParam String codeAsk,
                          @RequestParam float valueAsk,
                          @RequestParam String codeBid) {
        return String.format(
                "%.2f",
                exchangeService
                        .convertCurrency(
                                codeAsk,
                                valueAsk,
                                codeBid
                        )
        );
    }
}
