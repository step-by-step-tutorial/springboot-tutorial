package com.tutorial.springboot.abac.validation;

import com.tutorial.springboot.abac.exception.ValidationException;

import java.util.List;
import java.util.Objects;

public final class ObjectValidation {

    private ObjectValidation() {
    }

    public static void requireNotNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new ValidationException(List.of(errorMessage));
        }
    }
}
