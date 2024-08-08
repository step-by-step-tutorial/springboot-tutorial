package com.tutorial.springboot.abac.util;

import com.tutorial.springboot.abac.exception.ValidationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.tutorial.springboot.abac.validation.ObjectValidation.requireNotNull;
import static java.util.Objects.isNull;

public final class HttpUtils {

    private HttpUtils() {
    }

    public static <ID> URI createUriFromId(ID id) {
        requireNotNull(id, "ID should not be null");
        if (isNull(id)) {
            throw new ValidationException(List.of("ID should not be null"));
        }

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
