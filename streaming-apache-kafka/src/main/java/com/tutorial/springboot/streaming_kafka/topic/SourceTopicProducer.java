package com.tutorial.springboot.streaming_kafka.topic;

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
public class SourceTopicProducer {

    private final Logger logger = LoggerFactory.getLogger(SourceTopicProducer.class);

    private final KafkaTemplate<String, String> template;

    @Value("${topic.source}")
    private String topic;

    public SourceTopicProducer(final KafkaTemplate<String, String> template) {
        this.template = template;

        for (int i = 0; i < 10; i++) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            push("a" + 1);
        }

    }

    public void push(final String message) {
        requireNonNull(message, "message should not be null");

        Message<String> kafkaMessage = MessageBuilder
                .withPayload(message)
                .setHeader(KafkaHeaders.CORRELATION_ID, UUID.randomUUID().toString())
                .setHeader(KafkaHeaders.TOPIC, "sourceTopic")
                .build();

        template.send(kafkaMessage);
        logger.info("message sent: {}", message);
    }
}
