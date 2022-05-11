package pl.example.currencycalculator.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.example.currencycalculator.exceptions.InvalidCurrencyCodeException;
import pl.example.currencycalculator.exceptions.InvalidInputException;
import pl.example.currencycalculator.exceptions.NoContentException;
import pl.example.currencycalculator.model.dto.CurrencyDto;
import pl.example.currencycalculator.repo.ActivityLogRepo;

import java.util.HashSet;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class ExchangeServiceTest {

    @Mock
    ActivityLogRepo activityLogRepo;

    @InjectMocks
    private ActivityLogService activityLogService;

    @InjectMocks
    private ExchangeService exchangeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        exchangeService = new ExchangeService(new ActivityLogService(activityLogRepo));
    }


    @Test
    void convertCurrency_shouldReturnInvalidCurrencyCodeException_whenInvalidCodeIsGiven() {
        //given
        String codeAsk = "zloty";
        float valueAsk = 1;
        String codeBid = "dolar";

        //then
        Assertions.assertThrows(
                InvalidCurrencyCodeException.class,
                () -> {
                    //when
                    exchangeService.convertCurrency(codeAsk, valueAsk, codeBid);
                });
    }

    @Test
    void convertCurrency_shouldReturnInvalidInputException_whenNullCodeIsGiven() {
        // given
        String codeAsk = null;
        float valueAsk = 1;
        String codeBid = null;
        // then
        Assertions.assertThrows(
                InvalidCurrencyCodeException.class,
                () -> {
                    //when
                    exchangeService.convertCurrency(codeAsk, valueAsk, codeBid);
                });
    }

    @Test
    void convertCurrency_shouldReturnPositiveValue_whenGivenPositiveAskValue() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(Float.parseFloat(exchangeService.convertCurrency("USD", 1, "EUR")) > 0),
                () -> Assertions.assertTrue(Float.parseFloat(exchangeService.convertCurrency("CHF", 14, "AUD")) > 0),
                () -> Assertions.assertTrue(Float.parseFloat(exchangeService.convertCurrency("SGD", 22, "CAD")) > 0)
        );
    }

    @Test
    void getAllCodes_shouldReturnNotNullList_Always() throws NoContentException {
        Assertions.assertNotNull(exchangeService.getAllCodes());
    }

    @Test
    void getByCodes_shouldReturnList_whenGivenSetOfCodes() throws NoContentException, InvalidInputException, InvalidCurrencyCodeException {
        // given
        HashSet<String> codes = new HashSet<>() {{
            add("EUR");
            add("USD");
            add("AUD");
            add("CZK");
            add("DKK");
        }};
        // when
        List<CurrencyDto> result = exchangeService.getByCodes(codes);
        // then
        Assertions.assertEquals(codes.size(), result.size());
    }

}