package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RecordApplicationEvents
class EventPublisherServiceTest {

    @Autowired
    EventPublisherService systemUnderTest;

    @Autowired
    ApplicationEvents testAssistant;

    @Test
    void shouldThrowNullPointExceptionIfEventIsNull() {
        final var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.publish(null));

        assertNotNull(actual);
        assertEquals("Event should not be null", actual.getMessage());
    }

    @Test
    void shouldPublishEventSuccessfulWhenThereIsNoError() {
        final var givenEvent = new EventModel("content");

        final var actual = assertDoesNotThrow(
                () -> {
                    systemUnderTest.publish(givenEvent);
                    return testAssistant.stream(EventModel.class).toList();
                }
        );

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(givenEvent, actual.getFirst());
    }

}
