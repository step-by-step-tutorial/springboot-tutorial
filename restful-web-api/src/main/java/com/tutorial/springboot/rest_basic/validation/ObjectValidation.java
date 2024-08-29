package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;

import java.util.List;
import java.util.Objects;

public final class ObjectValidation {

    private ObjectValidation() {
    }

    public static void shouldNotBeNull(Object object, String errorMessage) {
        if (Objects.isNull(object)) {
            throw new ValidationException(List.of(errorMessage));
        }
    }

    public static void shouldNotBeNullOrEmpty(Object[] array, String errorMessage) {
        if (array == null || array.length == 0) {
            throw new ValidationException(List.of(errorMessage));
        }
    }

    public static void shouldBeEqual(Long n, Long m, String errorMessage) {
        if (!Objects.equals(n, m)) {
            throw new ValidationException(List.of(errorMessage));
        }
    }
}
