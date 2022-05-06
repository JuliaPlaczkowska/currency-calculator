package pl.example.currencycalculator.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.example.currencycalculator.model.dto.CurrencyDto;
import pl.example.currencycalculator.model.dto.TableDto;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class ExchangeService {

    private static final String URL = "https://api.nbp.pl/api/exchangerates/tables/a";
    private final RestTemplate restTemplate = new RestTemplate();

    private List<CurrencyDto> getAllAvailableCurrencies() {
        TableDto[] table = restTemplate.getForObject(URL, TableDto[].class);
        if (table != null && table.length > 0)
            return table[0].getRates();
        return new ArrayList<>();
    }

    public List<String> getAllCodes(){
         return getAllAvailableCurrencies()
                 .stream()
                 .map(CurrencyDto::getCode)
                 .collect(Collectors.toList());
    }
}
