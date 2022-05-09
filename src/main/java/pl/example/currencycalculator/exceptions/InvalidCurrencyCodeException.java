package pl.example.currencycalculator.exceptions;

public class InvalidCurrencyCodeException extends Exception{
    public InvalidCurrencyCodeException() {
        super("Invalid currency code");
    }
}
