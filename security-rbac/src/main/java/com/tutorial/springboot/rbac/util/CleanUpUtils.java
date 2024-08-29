package com.tutorial.springboot.rbac.util;

import com.tutorial.springboot.rbac.exception.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

import java.util.Objects;
import java.util.stream.Stream;

public final class CleanUpUtils {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private CleanUpUtils() {
    }

    public static <T> Stream<T> clean(Stream<T> stream, Class<?>... groups) {
        return stream.filter(Objects::nonNull)
                .filter((T object) -> validator.validate(object, groups).isEmpty())
                .distinct();
    }

    public static <T> void validate(Stream<T> stream, Class<?>... groups) {
        var errorMessages = stream.filter(Objects::nonNull)
                .map((T object) -> validator.validate(object, groups))
                .filter(errors -> !errors.isEmpty())
                .flatMap(errors -> errors.stream().map(ConstraintViolation::getMessage))
                .toList();

        if (!errorMessages.isEmpty()) {
            throw new ValidationException(errorMessages);
        }
    }
}
