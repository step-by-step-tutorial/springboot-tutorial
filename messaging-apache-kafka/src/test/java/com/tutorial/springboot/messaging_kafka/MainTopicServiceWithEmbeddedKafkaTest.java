package com.tutorial.springboot.messaging_kafka;

import com.tutorial.springboot.messaging_kafka.service.MainTopicService;
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
@DisplayName("unit tests of kafka[embedded] main topic service")
class MainTopicServiceWithEmbeddedKafkaTest {

    @Autowired
    MainTopicService systemUnderTest;

    @BeforeEach
    void setUp() {
        assertNotNull(systemUnderTest);
    }

    @Test
    @DisplayName("should throw a NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final String givenMessage = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "message should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenMessage));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the topic")
    void shouldPushMessageToTopic() {
        final var givenMessage = "hello";

        assertDoesNotThrow(() -> systemUnderTest.push(givenMessage));
    }

}
