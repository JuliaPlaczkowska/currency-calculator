package pl.example.currencycalculator.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ControllerAdvice
@RequestMapping("/api/v1")
public class ExceptionApi {

    private final String INVALID_REQUEST_BODY = "Invalid request body";

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<String> handleException() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(INVALID_REQUEST_BODY);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<String> handleException(MissingServletRequestParameterException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
