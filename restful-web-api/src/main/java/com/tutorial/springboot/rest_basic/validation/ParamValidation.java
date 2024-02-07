package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public final class ParamValidation {

    private ParamValidation() {
    }

    public static void requireNotEmpty(Collection<?> collection, String errorMessage) {
        if (collection.isEmpty()) {
            throw new ValidationException(List.of(errorMessage));
        }
    }

    public static void requireNotEmpty(Object[] array, String errorMessage) {
        if (array.length == 0) {
            throw new ValidationException(List.of(errorMessage));
        }
    }

    public static void requireNotNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new ValidationException(List.of(errorMessage));
        }
    }
}
