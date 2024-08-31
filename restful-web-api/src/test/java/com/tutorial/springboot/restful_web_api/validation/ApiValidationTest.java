package com.tutorial.springboot.restful_web_api.validation;

import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiValidationTest {

    @Test
    void givenNoErrors_thenValidationPasses() {
        var givenBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(givenBindingResult.hasErrors()).thenReturn(false);
        assertDoesNotThrow(() -> ApiValidation.shouldBeValid(givenBindingResult));
    }

    @Test
    void givenValidationErrors_thenThrowsException() {
        var givenBindingResult = Mockito.mock(BindingResult.class);
        Mockito.when(givenBindingResult.hasErrors()).thenReturn(true);
        Mockito.when(givenBindingResult.getAllErrors()).thenReturn(List.of(new ObjectError("object name", "error message")));

        var actual = assertThrows(ValidationException.class, () -> ApiValidation.shouldBeValid(givenBindingResult));

        assertNotNull(actual);
        assertNotNull(actual.getMessage());
        assertFalse(actual.getDetails().isEmpty());
    }
}