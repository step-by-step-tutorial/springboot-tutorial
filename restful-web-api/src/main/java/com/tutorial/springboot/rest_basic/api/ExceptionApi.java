package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionApi {

    @ExceptionHandler(value = {RuntimeException.class, NullPointerException.class, IllegalStateException.class, NoSuchElementException.class})
    public ResponseEntity<String> catchException(Exception ex) {
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<String>> catchValidationException(ValidationException ex) {
        return ResponseEntity.internalServerError().body(ex.getDetails());
    }

}
