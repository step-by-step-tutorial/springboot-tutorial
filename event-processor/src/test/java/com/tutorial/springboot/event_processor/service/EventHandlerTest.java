package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import com.tutorial.springboot.event_processor.config.LogCapture;
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
    ApplicationEventPublisher publisher;

    @Autowired
    LogCapture logInfoCapture;

    @Test
    @DisplayName("should receive an event")
    void shouldReceiveEventWhenThereIsNoError() {
        final var givenEvent = new EventModel("text");

        final var expectedLog = String.format("message received: %s", givenEvent.text());

        assertDoesNotThrow(() -> publisher.publishEvent(givenEvent));
        final var actual = logInfoCapture.getEvents();

        assertNotNull(actual);
        assertTrue(actual.contains(expectedLog));
    }

}
