package com.tutorial.springboot.rest_basic.api;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collection;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class ExceptionApi {

    private final Logger logger = LoggerFactory.getLogger(ExceptionApi.class.getSimpleName());

    @ExceptionHandler(value = {
            RuntimeException.class,
            NullPointerException.class,
            IllegalStateException.class,
            NoSuchElementException.class,
            IllegalArgumentException.class,
    })
    public ResponseEntity<String> catchException(Exception ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.internalServerError()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Collection<String>> catchValidationException(ValidationException ex) {
        logger.error(ex.getMessage());
        return ResponseEntity.badRequest()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(ex.getDetails());
    }

}
