package com.tutorial.springboot.messaging_rabbit_mq;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import com.tutorial.springboot.messaging_rabbit_mq.service.MainQueueService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@ActiveProfiles({"unit tests of main queue service"})
class MainQueueServiceTest {

    @Container
    static final RabbitMQContainer rabbitMqContainer = new RabbitMQContainer("rabbitmq:management");

    @DynamicPropertySource
    static void rabbitMQProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMqContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMqContainer::getAmqpPort);
        registry.add("spring.rabbitmq.username", rabbitMqContainer::getAdminUsername);
        registry.add("spring.rabbitmq.password", rabbitMqContainer::getAdminPassword);
    }

    @Autowired
    MainQueueService systemUnderTest;

    @BeforeAll
    static void beforeAll() {
        rabbitMqContainer.start();
    }

    @AfterAll
    static void afterAll() {
        rabbitMqContainer.start();
    }

    @Test
    @DisplayName("should throw a NullPointerException if the message is null")
    void shouldThrowNullPointerExceptionIfMessageIsNull() {
        final MessageModel givenModel = null;

        final var expectedException = NullPointerException.class;
        final var expectedExceptionMessage = "model should not be null";

        final var actual = assertThrows(expectedException, () -> systemUnderTest.push(givenModel));

        assertNotNull(actual);
        assertEquals(expectedExceptionMessage, actual.getMessage());
    }

    @Test
    @DisplayName("the message should be pushed to the queue")
    void messageShouldBePushedToTheQueue() {
        final var givenModel = new MessageModel(UUID.randomUUID().toString(), "test text");

        assertDoesNotThrow(() -> systemUnderTest.push(givenModel));
    }

}
