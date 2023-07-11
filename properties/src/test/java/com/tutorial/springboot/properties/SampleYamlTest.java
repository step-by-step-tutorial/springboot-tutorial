package com.tutorial.springboot.properties;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@DisplayName("unit tests of yaml file")
class SampleYamlTest {

    @Autowired
    SampleYaml systemUnderTest;

    @Test
    void GivenYamlPropertySourceFactory_WhenLoadedContext_ThenYamlPropertiesInjected() {
        assertNotNull(systemUnderTest);

        assertEquals(1, systemUnderTest.id());
        assertEquals("colorInfo", systemUnderTest.name());

        final var dateTime = LocalDateTime.parse(systemUnderTest.dateTime());
        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthValue());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(8, dateTime.getHour());
        assertEquals(0, dateTime.getMinute());
        assertEquals(0, dateTime.getSecond());

        final var colors = systemUnderTest.colors();
        assertEquals("red", colors[0]);
        assertEquals("green", colors[1]);
        assertEquals("blue", colors[2]);
    }
}
