package com.tutorial.springboot.properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DataPropertiesTest {

  private final DataProperties underTest;

  @Autowired
  DataPropertiesTest(DataProperties underTest) {
    this.underTest = underTest;
  }

  @Test
  void givenDefaultPropertySourceFactory_WhenLoadedContext_ThenPropertiesInjected() {
    assertNotNull(underTest);

    assertEquals(1, underTest.getId());
    assertEquals("colorInfo", underTest.getName());

    LocalDateTime dateTime = LocalDateTime.parse(underTest.getDateTime());

    assertEquals(2022, dateTime.getYear());
    assertEquals(1, dateTime.getMonthValue());
    assertEquals(1, dateTime.getDayOfMonth());
    assertEquals(8, dateTime.getHour());
    assertEquals(0, dateTime.getMinute());
    assertEquals(0, dateTime.getSecond());

    String[] colors = underTest.getColors();

    assertEquals("red", colors[0]);
    assertEquals("green", colors[1]);
    assertEquals("blue", colors[2]);
  }
}