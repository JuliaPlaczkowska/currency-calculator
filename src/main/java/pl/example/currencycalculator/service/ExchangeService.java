package pl.example.currencycalculator.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.example.currencycalculator.exceptions.InvalidCurrencyCodeException;
import pl.example.currencycalculator.exceptions.InvalidInputException;
import pl.example.currencycalculator.exceptions.NoContentException;
import pl.example.currencycalculator.model.dto.CurrencyDto;
import pl.example.currencycalculator.model.dto.TableDto;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@AllArgsConstructor
@Service
public class ExchangeService {

    private static final String URL = "https://api.nbp.pl/api/exchangerates/tables/a";
    private final RestTemplate restTemplate = new RestTemplate();

    private List<CurrencyDto> getAll() throws NoContentException {
        TableDto[] table = restTemplate.getForObject(URL, TableDto[].class);
        if (table == null)
            throw new NoContentException();
        else
            return table[0].getRates();
    }

    public List<CurrencyDto> getByCodes(Set<String> codes) throws NoContentException, InvalidInputException, InvalidCurrencyCodeException {
        if (codes == null || codes.size() <= 0)
            throw new InvalidInputException();
        List<CurrencyDto> all = getAll();

        if (!validCodes(codes, all))
            throw new InvalidCurrencyCodeException();

        return all
                .stream()
                .filter(
                        currency -> codes.contains(currency.getCode()))
                .collect(Collectors.toList());
    }

    public List<String> getAllCodes() throws NoContentException {
        return getAll()
                .stream()
                .map(CurrencyDto::getCode)
                .collect(Collectors.toList());
    }

    public String convertCurrency(String codeAsk,
                                  float valueAsk,
                                  String codeBid) throws NoContentException, InvalidInputException, InvalidCurrencyCodeException {

        float result = 0f;

        if (valueAsk != 0) {
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

            result = valueAsk * currencyAsk.getMid() / currencyBid.getMid();
        }
        String decimalFormat = "0.00";
        NumberFormat formatter = new DecimalFormat(decimalFormat);
        return formatter.format(result);
    }

    private boolean validCodes(Set<String> codes,
                               List<CurrencyDto> availableCurrencies) {
        List<String> availableCodes = availableCurrencies
                .stream().map(CurrencyDto::getCode)
                .collect(Collectors.toList());
        return availableCodes.containsAll(codes);
    }
}
