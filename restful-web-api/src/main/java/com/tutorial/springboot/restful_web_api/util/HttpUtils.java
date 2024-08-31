package com.tutorial.springboot.restful_web_api.util;

import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.tutorial.springboot.restful_web_api.validation.ObjectValidation.shouldNotBeNull;

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
