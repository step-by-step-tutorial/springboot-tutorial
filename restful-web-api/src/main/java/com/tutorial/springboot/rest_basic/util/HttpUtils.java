package com.tutorial.springboot.rest_basic.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.util.Objects.requireNonNull;

public final class HttpUtils {

    private HttpUtils() {
    }

    public static URI createUriFromId(Long id) {
        requireNonNull(id, "ID should not be null");
        if (id <= 0) {
            throw new IllegalStateException("ID should not be less than zero");
        }

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
