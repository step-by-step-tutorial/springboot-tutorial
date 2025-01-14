package com.tutorial.springboot.cdcdebezium.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class MainTopicListener {

    private final Logger logger = LoggerFactory.getLogger(MainTopicListener.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(@Payload String message) {
        requireNonNull(message, "message should not be null");

        logger.info("message received from {}", message);
    }
}
