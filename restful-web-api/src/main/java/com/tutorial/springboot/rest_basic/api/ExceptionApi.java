package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionApi {
    private final Logger logger = LoggerFactory.getLogger(ExceptionApi.class.getSimpleName());

    @ExceptionHandler(value = {RuntimeException.class, NullPointerException.class, IllegalStateException.class, NoSuchElementException.class})
    public ResponseEntity<String> catchException(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<List<String>> catchValidationException(ValidationException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getDetails());
    }

}
