package pl.example.currencycalculator.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TableDto {
    private String table;
    private String no;
    private LocalDate effectiveDate;
    private List<CurrencyDto> rates;
}
