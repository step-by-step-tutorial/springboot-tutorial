package com.tutorial.springboot.rest_basic.validation;

import java.util.Objects;

public final class NumberValidation {

    private NumberValidation() {
    }

    public static void requireEquality(Long n, Long m, String errorMessage) {
        if (!Objects.equals(n, m)) {
            throw new IllegalStateException(errorMessage);
        }

    }
}
