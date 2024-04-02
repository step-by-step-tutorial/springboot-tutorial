package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Collection Validation Unit Tests")
public class ObjectValidationTest {
    @Test
    public void givenNonNullObject_thenContinue() {
        var givenObject = new Object();
        assertDoesNotThrow(() -> ObjectValidation.requireNotNull(givenObject, "Object should not be empty"));
    }

    @Test
    public void givenNullObject_thenThrowValidationException() {
        final Object givenObject = null;
        var actual = assertThrows(ValidationException.class, () -> ObjectValidation.requireNotNull(givenObject, "Object should not be empty"));
        assertFalse(actual.getDetails().isEmpty());
    }
}