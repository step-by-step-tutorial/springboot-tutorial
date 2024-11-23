package com.tutorial.springboot.restful_web_api.dto;

import java.util.List;

public record Page<T>(
        List<T> content,

        int index,

        int totalItems,

        int size
) {
}
