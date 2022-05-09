package pl.example.currencycalculator.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.example.currencycalculator.model.dto.CurrencyDto;
import pl.example.currencycalculator.model.dto.TableDto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class ExchangeService {

    private static final String URL = "https://api.nbp.pl/api/exchangerates/tables/a";
    private final RestTemplate restTemplate = new RestTemplate();

    private List<CurrencyDto> getAll() {
        TableDto[] table = restTemplate.getForObject(URL, TableDto[].class);
        if (table != null && table.length > 0)
            return table[0].getRates();
        return new ArrayList<>();
    }

    public List<CurrencyDto> getByCodes(Set<String> codes) {
        List<CurrencyDto> all = getAll();
        return all
                .stream()
                .filter(
                        currency -> codes.contains(currency.getCode()))
                .collect(Collectors.toList());
    }

    public List<String> getAllCodes() {
        return getAll()
                .stream()
                .map(CurrencyDto::getCode)
                .collect(Collectors.toList());
    }

    public String convertCurrency(String codeAsk,
                                  float valueAsk,
                                  String codeBid) {

        List<CurrencyDto> currencies = getByCodes(new HashSet<String>() {{
            add(codeAsk);
            add(codeBid);
        }});

        CurrencyDto currencyAsk = new CurrencyDto(),
                currencyBid = new CurrencyDto();

        for (CurrencyDto currency :
                currencies)
            if (currency.getCode().equals(codeAsk))
                currencyAsk = currency;
            else currencyBid = currency;


        float result = valueAsk * currencyAsk.getMid() / currencyBid.getMid();
        String decimalFormat = "0.00";
        NumberFormat formatter = new DecimalFormat(decimalFormat);
        return formatter.format(result);
    }
}
