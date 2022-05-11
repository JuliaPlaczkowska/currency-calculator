package pl.example.currencycalculator.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import pl.example.currencycalculator.model.entity.ActivityLog;
import pl.example.currencycalculator.repo.ActivityLogRepo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static pl.example.currencycalculator.constants.ApiConstants.*;

@AllArgsConstructor
@Service
public class ActivityLogService {
    private ActivityLogRepo activityLogRepo;

    public void saveNbpGetTableA(String statusCode,
                                 LocalDateTime localDateTime) {
        save(NBP_API_URL,
                HttpMethod.GET.name(),
                NBP_TABLE_A_ENDPOINT,
                localDateTime,
                statusCode);
    }

    public void saveGetAllCurrencies(String statusCode,
                                     LocalDateTime localDateTime) {
        save(CURRENCY_CALCULATOR_API_URL,
                HttpMethod.GET.name(),
                GET_ALL_CURRENCIES,
                localDateTime,
                statusCode);
    }

    public void saveGetConvert(String statusCode,
                               LocalDateTime localDateTime) {
        save(CURRENCY_CALCULATOR_API_URL,
                HttpMethod.GET.name(),
                CONVERT,
                localDateTime,
                statusCode);
    }

    public void saveGetRatesByCodes(String statusCode,
                                    LocalDateTime localDateTime) {
        save(CURRENCY_CALCULATOR_API_URL,
                HttpMethod.GET.name(),
                RATES,
                localDateTime,
                statusCode);
    }

    private void save(String apiAddress,
                      String httpMethod,
                      String endpoint,
                      LocalDateTime localDateTime,
                      String statusCode) {
        activityLogRepo.save(
                ActivityLog.builder()
                        .apiAddress(apiAddress)
                        .httpMethod(httpMethod)
                        .endpoint(endpoint)
                        .timestamp(Timestamp.valueOf(localDateTime))
                        .statusCode(statusCode)
                        .build());
    }
}
