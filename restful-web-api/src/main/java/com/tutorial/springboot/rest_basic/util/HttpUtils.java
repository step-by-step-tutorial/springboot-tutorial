package com.tutorial.springboot.rest_basic.util;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

public final class HttpUtils {

    private HttpUtils() {
    }

    public static URI createUriFromId(Long id) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}
