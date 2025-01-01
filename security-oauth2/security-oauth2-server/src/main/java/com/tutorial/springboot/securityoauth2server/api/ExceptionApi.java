package com.tutorial.springboot.securityoauth2server.api;

import com.tutorial.springboot.securityoauth2server.dto.ErrorDto;
import com.tutorial.springboot.securityoauth2server.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.NoSuchElementException;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@RestControllerAdvice
public class ExceptionApi {

    private final Logger logger = LoggerFactory.getLogger(ExceptionApi.class.getSimpleName());

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorDto> catchValidationException(ValidationException ex) {
        var details = ex.getDetails().toArray(new String[0]);
        logger.error(Arrays.toString(details));
        return ResponseEntity.badRequest()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorDto(details));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> catchBadCredentialsException(BadCredentialsException ex) {
        var message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        logger.error(message);
        return ResponseEntity.status(UNAUTHORIZED)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorDto(message));
    }

    @ExceptionHandler(value = {
            RuntimeException.class,
            NullPointerException.class,
            IllegalStateException.class,
            NoSuchElementException.class,
            IllegalArgumentException.class
    })
    public ResponseEntity<ErrorDto> catchException(Exception ex) {
        var message = ex.getMessage() == null ? ex.getClass().getSimpleName() : ex.getMessage();
        logger.error(message);
        return ResponseEntity.internalServerError()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(new ErrorDto(message));
    }

}
