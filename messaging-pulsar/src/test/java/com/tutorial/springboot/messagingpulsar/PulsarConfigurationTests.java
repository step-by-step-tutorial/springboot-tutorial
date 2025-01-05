package com.tutorial.springboot.messagingpulsar;

import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionInitialPosition;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PulsarContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.TimeUnit;

@SpringBootTest
@ActiveProfiles("test")
class PulsarConfigurationTests {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
    private PulsarClient pulsarClient;

    @Autowired
    PulsarTemplate<String> template;

    @Test
    void sendAndReceiveWorksAsExpected() {
        var givenTopicName = "test-topic";
        var givenMessage = "test message";

        try {
            pulsarClient.newProducer(Schema.STRING)
                    .topic(givenTopicName)
                    .create()
                    .close();

            var consumer = pulsarClient.newConsumer(Schema.STRING)
                    .topic(givenTopicName)
                    .subscriptionName("test-subscription")
                    .subscriptionInitialPosition(SubscriptionInitialPosition.Earliest)
                    .subscribe();
            logger.info("Consumer subscribed to topic: {}", givenTopicName);
            Thread.sleep(1000);

            template.send(givenTopicName, givenMessage);
            logger.info("Message sent: {}", givenMessage);

            var message = consumer.receive(10, TimeUnit.SECONDS);
            if (message != null) {
                logger.info("Message received: {}", message.getValue());
                consumer.acknowledge(message);
            } else {
                logger.warn("No message received within the timeout period.");
            }
        } catch (Exception e) {
            logger.error("Error during test execution", e);
            throw new RuntimeException(e);
        }

        logger.info("Test is completed.");
    }

}