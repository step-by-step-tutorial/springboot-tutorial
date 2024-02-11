package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;

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
