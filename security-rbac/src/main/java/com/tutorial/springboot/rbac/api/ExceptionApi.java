package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionApi {
    private final Logger logger = LoggerFactory.getLogger(ExceptionApi.class.getSimpleName());

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Collection<String>> catchValidationException(ValidationException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getDetails());
    }

    @ExceptionHandler(value = {RuntimeException.class, NullPointerException.class, IllegalStateException.class, NoSuchElementException.class})
    public ResponseEntity<String> catchException(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError().body(ex.getMessage());
    }

}
