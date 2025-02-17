package com.tutorial.springboot.messaging_rabbit_mq.service;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@DisplayName("unit tests of rabbitmq main queue service")
class MainQueueServiceWithContainerTest {

    @Container
    static final RabbitMQContainer RABBITMQ = new RabbitMQContainer("rabbitmq:management");

    @DynamicPropertySource
    static void rabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", RABBITMQ::getHost);
        registry.add("spring.rabbitmq.port", RABBITMQ::getAmqpPort);
        registry.add("spring.rabbitmq.username", RABBITMQ::getAdminUsername);
        registry.add("spring.rabbitmq.password", RABBITMQ::getAdminPassword);
    }

    @Autowired
    MainQueueService systemUnderTest;

    @BeforeAll
    static void beforeAll() {
        RABBITMQ.start();
    }

    @AfterAll
    static void afterAll() {
        RABBITMQ.start();
    }

    @Test
    @DisplayName("should throw a NullPointerException when given message is null")
    void shouldThrowNullPointerExceptionWhenMessageIsNull() {
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.publish(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.publish(givenModel));
    }

}
