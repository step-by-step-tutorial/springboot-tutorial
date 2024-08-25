package com.tutorial.springboot.rbac.exception;

import java.util.Collection;

public class ArrayOfValidationException extends RuntimeException {
    private final Collection<String> details;

    public ArrayOfValidationException(Collection<String> errorMessages) {
        super("There are invalid values, please refer to the details!");
        this.details = errorMessages;
    }

    public Collection<String> getDetails() {
        return details;
    }
}
