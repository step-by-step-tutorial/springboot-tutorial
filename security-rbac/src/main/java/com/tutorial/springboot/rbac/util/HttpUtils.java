package com.tutorial.springboot.rbac.util;

import com.tutorial.springboot.rbac.exception.ArrayOfValidationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.tutorial.springboot.rbac.validation.ObjectValidation.requireNotNull;
import static java.util.Objects.isNull;

public final class HttpUtils {

    private HttpUtils() {
    }

    public static <ID> URI createUriFromId(ID id) {
        requireNotNull(id, "ID should not be null");
        if (isNull(id)) {
            throw new ArrayOfValidationException(List.of("ID should not be null"));
        }

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
