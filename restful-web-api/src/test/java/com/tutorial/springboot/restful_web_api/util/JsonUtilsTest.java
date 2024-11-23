package com.tutorial.springboot.restful_web_api.util;

import com.tutorial.springboot.restful_web_api.entity.SampleEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Json Utilities Unit Tests")
public class JsonUtilsTest {

    static SampleEntity createObject() {
        return new SampleEntity()
                .id(1L)
                .version(1)
                .text("Sample Text")
                .code(100)
                .datetime(LocalDateTime.of(2024, 1, 1, 12, 0));
    }

    @Test
    void givenEntity_whenToJsonString_thenConvertSuccessfully() {
        var givenObject = createObject();

        var actual = JsonUtils.toJsonString(givenObject);

        assertNotNull(actual);
        assertTrue(actual.contains("\"id\":1"));
        assertTrue(actual.contains("\"version\":1"));
        assertTrue(actual.contains("\"text\":\"Sample Text\""));
        assertTrue(actual.contains("\"code\":100"));
        assertTrue(actual.contains("\"datetime\":[2024,1,1,12,0]"));
    }

    @Test
    void givenNull_whenToJsonString_thenReturnNull() {
        Object givenObject = null;

        var actual = JsonUtils.toJsonString(givenObject);
        assertEquals("null", actual, "Serialization of null should result in 'null'");
    }

    @Test
    void givenSelfReferenceObject_whenToJsonString_thenReturnExceptionMessage() {
        class Node {
            Node self;
        }

        var node = new Node();
        node.self = node;

        var result = JsonUtils.toJsonString(node);
        assertTrue(result.contains("cycle"), "Expected error message indicating a cyclic reference issue");

    }
}