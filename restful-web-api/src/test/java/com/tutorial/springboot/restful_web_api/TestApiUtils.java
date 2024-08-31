package com.tutorial.springboot.restful_web_api;

import org.springframework.web.util.UriComponentsBuilder;

public final class TestApiUtils {

    private TestApiUtils() {
    }

    public static UriComponentsBuilder uriBuilder(int port, String basePath) {
        return UriComponentsBuilder.newInstance()
                .encode()
                .scheme("http")
                .host("localhost")
                .port(port)
                .path(basePath);
    }

}
