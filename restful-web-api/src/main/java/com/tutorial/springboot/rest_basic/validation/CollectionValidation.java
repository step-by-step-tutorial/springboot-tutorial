package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;

import java.util.Collection;
import java.util.List;

public final class CollectionValidation {

    private CollectionValidation() {
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

}
