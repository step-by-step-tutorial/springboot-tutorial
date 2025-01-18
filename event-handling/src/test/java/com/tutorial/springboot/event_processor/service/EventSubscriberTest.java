package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.config.TestLogRepository;
import com.tutorial.springboot.event_processor.model.EventModel;
import com.tutorial.springboot.event_processor.model.LogSubject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("unit tests of event handler")
class EventSubscriberTest {

    @Autowired
    ApplicationEventPublisher publisher;

    @Autowired
    private TestLogRepository testLogRepository;

    @Test
    @DisplayName("should receive an event")
    void shouldReceiveEventWhenThereIsNoError() {
        final var givenEvent = new EventModel("content");

        final var expectedLog = String.format("%s: %s", LogSubject.INPUT_EVENT.getMessage(), givenEvent.content());

        final var actual = assertDoesNotThrow(() ->
                {
                    publisher.publishEvent(givenEvent);
                    return testLogRepository.getMessages();
                }
        );

        assertNotNull(actual);
        assertTrue(actual.contains(expectedLog));
    }

}
