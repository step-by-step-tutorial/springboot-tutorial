package com.tutorial.springboot.messaging_kafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class MainTopicListener {

    private final Logger logger = LoggerFactory.getLogger(MainTopicListener.class);

    @KafkaListener(topics = "${spring.kafka.topic.name}", groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(
            @Payload String message,
            @Header(KafkaHeaders.CORRELATION_ID) String correlationId
    ) {
        requireNonNull(message, "message should not be null");
        requireNonNull(correlationId, "correlation Id should not be null");

        logger.info("Message received {}: {}", correlationId, message);
    }
}
