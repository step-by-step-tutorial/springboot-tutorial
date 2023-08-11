package com.tutorial.springboot.event_processor.service;

import com.tutorial.springboot.event_processor.model.EventModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RecordApplicationEvents
@DisplayName("unit tests of event publisher")
class EventPublisherTest {

    @Autowired
    EventPublisher systemUnderTest;

    @Autowired
    ApplicationEvents applicationEvents;

    @Test
    @DisplayName("should throw a NullPointException if the event is null")
    void shouldThrowNullPointExceptionIfEventIsNull() {
        final EventModel givenEvent = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "event should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.publish(givenEvent));

        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the event should publish successful")
    void shouldPublishEventSuccessfulWhenThereIsNoError() {
        final var givenEvent = new EventModel("fake text");

        assertDoesNotThrow(() -> systemUnderTest.publish(givenEvent));
        final var actual = applicationEvents.stream(EventModel.class).toList();

        assertNotNull(actual);
        assertEquals(1, actual.size());
        assertEquals(givenEvent, actual.get(0));
    }

}
