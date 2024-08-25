package com.tutorial.springboot.rbac.validation;

import com.tutorial.springboot.rbac.exception.ArrayOfValidationException;

import java.util.Collection;
import java.util.List;

public final class CollectionValidation {

    private CollectionValidation() {
    }

    public static void requireNotEmpty(Collection<?> collection, String errorMessage) {
        if (collection.isEmpty()) {
            throw new ArrayOfValidationException(List.of(errorMessage));
        }
    }

    public static void requireNotEmpty(Object[] array, String errorMessage) {
        if (array.length == 0) {
            throw new ArrayOfValidationException(List.of(errorMessage));
        }
    }

}
