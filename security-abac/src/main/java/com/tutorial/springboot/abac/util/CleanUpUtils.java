package com.tutorial.springboot.abac.util;

import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Objects;
import java.util.stream.Stream;

public final class CleanUpUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private CleanUpUtils() {
    }

    public static <T> Stream<T> clean(Stream<T> stream) {
        return stream
                .filter(Objects::nonNull)
                .filter((T object) -> validator.validate(object).isEmpty())
                .distinct();
    }
}
