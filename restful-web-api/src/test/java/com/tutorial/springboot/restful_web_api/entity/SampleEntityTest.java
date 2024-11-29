package com.tutorial.springboot.restful_web_api.entity;

import com.tutorial.springboot.restful_web_api.dto.SampleDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class SampleEntityTest {

    static SampleEntity createEntity() {
        return new SampleEntity()
                .id(1L)
                .version(1)
                .text("Sample Text")
                .code(100)
                .datetime(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    static SampleEntity createOtherEntity() {
        return new SampleEntity()
                .id(2L)
                .version(1)
                .text("Different Text")
                .code(200)
                .datetime(LocalDateTime.of(2024, 1, 2, 12, 0));
    }

    @Test
    void givenEntity_whenToString_thenNotNull() {
        var givenEntity = createEntity();

        var actual = givenEntity.toString();

        assertNotNull(actual);
        assertTrue(actual.contains("\"id\":1"));
        assertTrue(actual.contains("\"version\":1"));
        assertTrue(actual.contains("\"text\":\"Sample Text\""));
        assertTrue(actual.contains("\"code\":100"));
        assertTrue(actual.contains("\"datetime\":[2024,1,1,12,0]"));
    }

    @Test
    void givenTwoEntities_whenHashCode_thenEquals() {
        var givenEntity = createEntity();
        var givenOtherEntity = createEntity();

        assertEquals(givenEntity.hashCode(), givenOtherEntity.hashCode());
    }

    @Test
    void givenTwoEntities_whenEquals_thenEqual() {
        var givenEntity = createEntity();
        var givenOtherEntity = createEntity();

        var actual = givenEntity.equals(givenOtherEntity);

        assertTrue(actual);
    }

    @Test
    void givenTwoDifferentEntities_whenEquals_thenNotEqual() {
        var givenEntity = createEntity();
        var givenOtherEntity = createOtherEntity();

        var actual = givenEntity.equals(givenOtherEntity);

        assertFalse(actual);
    }

    @Test
    void givenNullAndEntity_whenEquals_thenNotEqual() {
        var givenEntity = createEntity();

        var actual = givenEntity.equals(null);

        assertFalse(actual);
    }

    @Test
    void givenTwoDifferentType_whenEquals_thenNotEqual() {
        var givenEntity = createEntity();
        var givenOtherType = SampleDto.builder().build();

        var actual = givenEntity.equals(givenOtherType);

        assertFalse(actual);
    }

    @Test
    void givenEntityAndOtherEntity_whenUpdateFrom_thenUpdated() {
        var givenEntity = createEntity();
        var givenNewOneEntity = createOtherEntity();

        givenEntity.updateFrom(givenNewOneEntity);

        assertEquals(2, givenEntity.version());
        assertEquals(givenNewOneEntity.text(), givenEntity.text());
        assertEquals(givenNewOneEntity.code(), givenEntity.code());
        assertEquals(givenNewOneEntity.datetime(), givenEntity.datetime());
    }

    @Test
    void givenEntityAndVersionMismatchEntity_whenUpdateFrom_thenIllegalStateException() {
        var givenEntity = createEntity();
        var givenOtherVersionEntity = createEntity().version(2);

        var actual = assertThrows(IllegalStateException.class, () -> givenEntity.updateFrom(givenOtherVersionEntity));

        assertTrue(actual.getMessage().contains("entity [id 1 and version 1] do not match with new update"));
    }
}
