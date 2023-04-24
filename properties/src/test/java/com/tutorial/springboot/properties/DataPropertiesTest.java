package com.tutorial.springboot.properties;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class DataPropertiesTest {

    @Autowired
    private DataProperties underTest;

    @Test
    void GivenDefaultPropertySourceFactory_WhenLoadedContext_ThenPropertiesInjected() {
        assertNotNull(underTest);

        assertEquals(1, underTest.id());
        assertEquals("colorInfo", underTest.name());

        LocalDateTime dateTime = LocalDateTime.parse(underTest.dateTime());

        assertEquals(2022, dateTime.getYear());
        assertEquals(1, dateTime.getMonthValue());
        assertEquals(1, dateTime.getDayOfMonth());
        assertEquals(8, dateTime.getHour());
        assertEquals(0, dateTime.getMinute());
        assertEquals(0, dateTime.getSecond());

        String[] colors = underTest.colors();

        assertEquals("red", colors[0]);
        assertEquals("green", colors[1]);
        assertEquals("blue", colors[2]);
    }
}