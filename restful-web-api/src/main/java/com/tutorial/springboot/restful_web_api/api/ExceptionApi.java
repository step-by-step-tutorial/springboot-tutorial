package com.tutorial.springboot.restful_web_api.api;

import com.tutorial.springboot.restful_web_api.dto.ErrorDto;
import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public ResponseEntity<ErrorDto> catchException(Exception ex) {
        logger.error("ExceptionApi catch a general exception: {}", ex.getMessage());
        return ResponseEntity.internalServerError()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> catchValidationException(ValidationException ex) {
        logger.error("ExceptionApi catch a validation exception: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorDto(ex.getDetails().toArray(String[]::new)));
    }

}
