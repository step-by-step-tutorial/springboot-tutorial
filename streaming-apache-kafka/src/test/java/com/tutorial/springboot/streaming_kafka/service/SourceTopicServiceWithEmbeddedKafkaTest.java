package com.tutorial.springboot.streaming_kafka.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"embedded-kafka"})
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9095", "port=9095"})
class SourceTopicServiceWithEmbeddedKafkaTest {

    @Autowired
    SourceTopicService systemUnderTest;

    @BeforeEach
    void setUp() {
        assertNotNull(systemUnderTest);
    }

    @Test
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final String givenMessage = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "Message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    void shouldPushMessageToTopic() {
        final var givenMessage = "hello";

        assertDoesNotThrow(() -> systemUnderTest.push(givenMessage));
    }

}
