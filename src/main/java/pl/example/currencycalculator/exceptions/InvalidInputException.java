package pl.example.currencycalculator.exceptions;

public class InvalidInputException extends Exception{
    public InvalidInputException() {
        super("Invalid input");
    }
}
