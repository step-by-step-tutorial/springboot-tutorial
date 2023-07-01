package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.testUtils.LogCapture;
import com.tutorial.springboot.event_processor.testUtils.TestLogUtils;
import org.apache.logging.log4j.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("unit tests of event handler")
class EventHandlerTest {

    @Autowired
    private ApplicationEventPublisher publisher;

    private final LogCapture logCapture = new LogCapture();

    @BeforeEach
    void setUp() {
        TestLogUtils.addAppender(logCapture, Level.INFO);
        logCapture.start();
    }

    @AfterEach
    void tearDown() {
        logCapture.stop();
    }

    @Test
    @DisplayName("should receive and handle the event")
    void shouldReceiveAndHandleEvent() {
        final var givenEvent = FakeDataFactory.FAKE_EVENT_MODEL;

        final var expectedLog = String.format("message received: %s", givenEvent.text());

        assertDoesNotThrow(() -> publisher.publishEvent(givenEvent));
        final var actual = logCapture.getEvents();

        assertNotNull(actual);
        assertTrue(actual.contains(expectedLog));
    }

}
