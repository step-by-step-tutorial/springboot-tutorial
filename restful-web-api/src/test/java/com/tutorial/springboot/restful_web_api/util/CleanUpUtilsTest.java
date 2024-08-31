package com.tutorial.springboot.restful_web_api.util;

import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.Test;

import static com.tutorial.springboot.restful_web_api.util.CleanUpUtils.clean;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CleanUpUtilsTest {

    private record StubDto(@NotNull Integer id) {
    }

    @Test
    void givenNullObjectsOnly_whenClean_thenExpectZeroCount() {
        var givenStream = new StubDto[]{null, null};
        var actual = clean(givenStream);
        assertEquals(0, actual.count());
    }

    @Test
    void givenNullAndNonNullObjects_whenClean_thenExpectCountOfNotNullObjects() {
        var givenStream = new StubDto[]{new StubDto(1), null};
        var actual = clean(givenStream);
        assertEquals(1, actual.count());
    }

    @Test
    void givenValidAndInvalidObjects_whenClean_thenExpectCountOfValidObjects() {
        var givenStream = new StubDto[]{new StubDto(1), new StubDto(null)};
        var actual = clean(givenStream);
        assertEquals(1, actual.count());
    }

    @Test
    void givenOnlyInvalidObjects_whenClean_thenExpectZeroCount() {
        var givenStream = new StubDto[]{new StubDto(null), new StubDto(null)};
        var actual = clean(givenStream);
        assertEquals(0, actual.count());
    }

}