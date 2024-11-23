package com.tutorial.springboot.restful_web_api.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SampleDtoTest {

    @Test
    void givenValidInputs_whenBuilderBuildsSampleDto_thenSampleDtoIsBuiltCorrectly() {
        var id = 1L;
        var text = "Sample Text";
        var code = 123;
        var datetime = LocalDateTime.now();
        var version = 1;

        var actual = SampleDto.builder()
                .id(id)
                .text(text)
                .code(code)
                .datetime(datetime)
                .version(version)
                .build();

        assertNotNull(actual);
        assertEquals(id, actual.id());
        assertEquals(text, actual.text());
        assertEquals(code, actual.code());
        assertEquals(datetime, actual.datetime());
        assertEquals(version, actual.version());
    }

    @Test
    void givenNullInputs_whenBuilderBuildsSampleDto_thenFieldsAreNull() {
        var actual = SampleDto.builder()
                .id(null)
                .text(null)
                .code(null)
                .datetime(null)
                .version(null)
                .build();

        assertNotNull(actual);
        assertNull(actual.id());
        assertNull(actual.text());
        assertNull(actual.code());
        assertNull(actual.datetime());
        assertNull(actual.version());
    }

    @Test
    void givenExistingSampleDto_whenFromMethodIsUsed_thenPropertiesAreCopied() {
        var givenDto = SampleDto.builder()
                .id(1L)
                .text("Original Text")
                .code(456)
                .datetime(LocalDateTime.now())
                .version(2)
                .build();

        var actual = SampleDto.builder()
                .from(givenDto)
                .build();

        assertNotNull(actual);
        assertEquals(givenDto.id(), actual.id());
        assertEquals(givenDto.text(), actual.text());
        assertEquals(givenDto.code(), actual.code());
        assertEquals(givenDto.datetime(), actual.datetime());
        assertEquals(givenDto.version(), actual.version());
    }

    @Test
    void givenExistingSampleDto_whenFromMethodIsUsedAndModified_thenModifiedDtoIsBuilt() {
        var givenDto = SampleDto.builder()
                .id(1L)
                .text("Original Text")
                .code(456)
                .datetime(LocalDateTime.now())
                .version(2)
                .build();

        var actual = SampleDto.builder()
                .from(givenDto)
                .text("Modified Text")
                .build();

        assertNotNull(actual);
        assertEquals(givenDto.id(), actual.id());
        assertEquals("Modified Text", actual.text());
        assertEquals(givenDto.code(), actual.code());
        assertEquals(givenDto.datetime(), actual.datetime());
        assertEquals(givenDto.version(), actual.version());
    }
}
