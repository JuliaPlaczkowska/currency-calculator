package pl.example.currencycalculator.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import pl.example.currencycalculator.exceptions.InvalidCurrencyCodeException;
import pl.example.currencycalculator.exceptions.InvalidInputException;
import pl.example.currencycalculator.exceptions.NoContentException;
import pl.example.currencycalculator.service.ExchangeService;

import java.util.Set;

@AllArgsConstructor
@RestController
@RequestMapping("api/v1")
public class ExchangeController {

    private final String FAILED_TO_LOAD_DATA = "Failed to load data";

    private final ExchangeService exchangeService;

    @GetMapping("/currencies")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(exchangeService.getAllCodes());
        } catch (NoContentException e) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FAILED_TO_LOAD_DATA);
        }
    }

    @GetMapping("/convert")
    public ResponseEntity<?> convert(@RequestParam String codeAsk,
                                     @RequestParam float valueAsk,
                                     @RequestParam String codeBid) {
        String conversionResult = null;
        try {
            conversionResult = exchangeService
                    .convertCurrency(
                            codeAsk,
                            valueAsk,
                            codeBid
                    );
            return ResponseEntity.ok(conversionResult);
        } catch (NoContentException e) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FAILED_TO_LOAD_DATA);
        } catch (InvalidInputException | InvalidCurrencyCodeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/rates")
    public ResponseEntity<?> getRatesForCurrencySet(@RequestParam Set<String> codes) {
        try {
            return ResponseEntity.ok(exchangeService.getByCodes(codes));
        } catch (NoContentException e) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(e.getMessage());
        } catch (HttpClientErrorException e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(FAILED_TO_LOAD_DATA);
        } catch (InvalidInputException | InvalidCurrencyCodeException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
