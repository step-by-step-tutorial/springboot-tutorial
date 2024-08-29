package com.tutorial.springboot.rest_basic.dto;

import java.util.List;

public record Page<T>(
        List<T> items,

        int currentPage,

        int totalItems,

        int totalPages
) {
}
