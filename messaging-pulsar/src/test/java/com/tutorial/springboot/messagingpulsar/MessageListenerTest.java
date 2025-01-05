package com.tutorial.springboot.messagingpulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import static com.tutorial.springboot.messagingpulsar.MessengerConstance.TOPIC_NAME;
import static org.mockito.Mockito.verify;

@SpringBootTest
@ActiveProfiles("test")
class MessageListenerTest {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerTest.class);

    @Container
    static final PulsarContainer PULSAR_CONTAINER = new PulsarContainer(DockerImageName.parse("apachepulsar/pulsar:latest"));

    @DynamicPropertySource
    static void pulsarProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.pulsar.client.service-url", PULSAR_CONTAINER::getPulsarBrokerUrl);
        registry.add("spring.pulsar.admin.service-url", PULSAR_CONTAINER::getHttpServiceUrl);
    }

    @BeforeAll
    static void setUp() {
        PULSAR_CONTAINER.start();
    }

    @AfterAll
    static void tearDown() {
        PULSAR_CONTAINER.stop();
    }

    @Autowired
    private MessageService messageService;

    @Test
    void testMessageListenerReceivesMessage() throws InterruptedException {
        var testMessage = "test message";

        logger.info("Sending message to topic: {}", TOPIC_NAME);
        messageService.send(testMessage);

        Thread.sleep(2000);
    }
}
