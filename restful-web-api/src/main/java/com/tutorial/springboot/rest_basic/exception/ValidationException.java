package com.tutorial.springboot.rest_basic.exception;

import java.util.List;

public class ValidationException extends RuntimeException {
    private final List<String> details;

    public ValidationException(List<String> errorMessages) {
        super("There are invalid values, please refer to the details!");
        this.details = errorMessages;
    }

    public List<String> getDetails() {
        return details;
    }
}
