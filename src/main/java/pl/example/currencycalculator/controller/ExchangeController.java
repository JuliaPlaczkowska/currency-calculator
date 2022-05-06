package pl.example.currencycalculator.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import pl.example.currencycalculator.model.Currency;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ExchangeController {

    private final List<Currency> currencies;

    public ExchangeController(){
        this.currencies = new ArrayList<>();
        currencies.add(new Currency("bat (Tajlandia)", "THB", (float) 0.1294));
        currencies.add(new Currency("dolar amerykański", "USD", (float) 4.4502));
        currencies.add(new Currency("dolar australijski", "AUD", (float) 3.1543));
    }

    @GetMapping("/currencies")
    public List<Currency> getAll(){
        return currencies;
    }
}
