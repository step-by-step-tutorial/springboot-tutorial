package com.tutorial.springboot.rest_basic.validation;

import com.tutorial.springboot.rest_basic.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Collection Validation Unit Tests")
public class CollectionValidationTest {
    @Nested
    class RequireNotEmptyFunction {
        @Test
        public void givenNonEmptyCollection_thenContinue() {
            var givenCollection = List.of("fake item");
            assertDoesNotThrow(() -> CollectionValidation.requireNotEmpty(givenCollection, "List should not be empty"));
        }

        @Test
        public void givenEmptyCollection_thenThrowValidationException() {
            var givenCollection = Collections.emptyList();
            var actual = assertThrows(ValidationException.class, () -> CollectionValidation.requireNotEmpty(givenCollection, "List should not be empty"));
            assertFalse(actual.getDetails().isEmpty());
        }
    }

    @Nested
    class ArrayRequireNotEmpty {
        @Test
        public void givenNonEmptyArray_thenContinue() {
            String[] givenArray = {"fake item"};
            assertDoesNotThrow(() -> CollectionValidation.requireNotEmpty(givenArray, "Array should not be empty"));
        }

        @Test
        public void givenEmptyArray_thenThrowValidationException() {
            String[] givenArray = {};
            var actual = assertThrows(ValidationException.class, () -> CollectionValidation.requireNotEmpty(givenArray, "Array should not be empty"));
            assertFalse(actual.getDetails().isEmpty());
        }
    }
}