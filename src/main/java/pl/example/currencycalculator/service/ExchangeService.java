package pl.example.currencycalculator.service;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.example.currencycalculator.exceptions.InvalidCurrencyCodeException;
import pl.example.currencycalculator.exceptions.InvalidInputException;
import pl.example.currencycalculator.exceptions.NoContentException;
import pl.example.currencycalculator.model.dto.CurrencyDto;
import pl.example.currencycalculator.model.dto.TableDto;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static pl.example.currencycalculator.constants.ApiConstants.NBP_API_URL;
import static pl.example.currencycalculator.constants.ApiConstants.NBP_TABLE_A_ENDPOINT;


@AllArgsConstructor
@Service
public class ExchangeService {

    private static final String URL = NBP_API_URL + NBP_TABLE_A_ENDPOINT;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ActivityLogService activityLogService;

    private TableDto[] getTableAFromNbpApi(){
        ResponseEntity<TableDto[]> response = restTemplate.getForEntity(URL, TableDto[].class);
        HttpStatus statusCode = response.getStatusCode();
        activityLogService.saveNbpGetTableA(statusCode.toString(), LocalDateTime.now());

        return response.getBody();
    }

    private List<CurrencyDto> getAll() throws NoContentException {
        TableDto[] table = getTableAFromNbpApi();
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

        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);

        String decimalFormat = "0.00";
        NumberFormat formatter = new DecimalFormat(decimalFormat, symbols);
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
