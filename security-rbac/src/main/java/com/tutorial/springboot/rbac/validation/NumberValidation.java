package com.tutorial.springboot.rbac.validation;

import com.tutorial.springboot.rbac.exception.ValidationException;

import java.util.List;
import java.util.Objects;

public final class NumberValidation {

    private NumberValidation() {
    }

    public static void requireEquality(Long n, Long m, String errorMessage) {
        if (!Objects.equals(n, m)) {
            throw new ValidationException(List.of(errorMessage));
        }
    }
}
