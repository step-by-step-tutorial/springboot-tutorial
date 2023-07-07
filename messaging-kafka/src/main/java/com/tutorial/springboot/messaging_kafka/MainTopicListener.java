package com.tutorial.springboot.messaging_kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class MainTopicListener {

    private final Logger logger = LoggerFactory.getLogger(MainTopicListener.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(final String message) {
        requireNonNull(message);
        logger.info("message received: {}", message);
    }
}
