package com.tutorial.springboot.securityoauth2client.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

import static java.util.Objects.requireNonNull;

public final class HttpUtils {

    private HttpUtils() {
    }

    public static <ID> URI createUriFromId(ID id) {
        requireNonNull(id, "URI: ID should not be null");

        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
