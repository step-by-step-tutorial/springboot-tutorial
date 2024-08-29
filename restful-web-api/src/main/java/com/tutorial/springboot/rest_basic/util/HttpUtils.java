package com.tutorial.springboot.rest_basic.util;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.tutorial.springboot.rest_basic.validation.ObjectValidation.shouldNotBeNull;

public final class HttpUtils {

    private HttpUtils() {
    }

    public static URI uriOf(Long id) {
        shouldNotBeNull(id, "ID should not be null");
        if (id <= 0) {
            throw new ValidationException(List.of("ID should not be less than zero"));
        }

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
