package com.tutorial.springboot.streaming_kafka.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static java.util.Objects.requireNonNull;

@Service
public class SourceTopicService {

    private final Logger logger = LoggerFactory.getLogger(SourceTopicService.class);

    private final KafkaTemplate<String, String> template;

    @Value("${topic.source}")
    private String topic;

    public SourceTopicService(final KafkaTemplate<String, String> template) {
        this.template = template;
    }

    public void push(final String message) {
        requireNonNull(message, "Message should not be null");

        Message<String> kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString())
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build();

        template.send(kafkaMessage);
        logger.info("Message sent to {}: {}", topic, message);
    }
}
