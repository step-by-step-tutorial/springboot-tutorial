package com.tutorial.springboot.cdcdebezium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
@Profile({"kafka", "embedded-kafka"})
public class ExampleTopicService {

    private final Logger logger = LoggerFactory.getLogger(ExampleTopicService.class);

    private final KafkaTemplate<String, String> template;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    @Autowired
    public ExampleTopicService(final KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public void push(final String message) {
        requireNonNull(message, "message should not be null");

        var kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString())
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        template.send(kafkaMessage);
        logger.info("Message sent: {}", message);
    }
}
