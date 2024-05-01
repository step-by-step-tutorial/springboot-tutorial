package com.tutorial.springboot.streaming_kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class OutputTopicListener {

    private final Logger logger = LoggerFactory.getLogger(OutputTopicListener.class);

    @KafkaListener(topics = "${topic.output}")
    public void onMessage(
            @Payload String message,
            @Header(KafkaHeaders.CORRELATION_ID) String correlationId
    ) {
        requireNonNull(message, "message should not be null");
        requireNonNull(correlationId, "correlation Id should not be null");

        logger.info("message received from {}: {}", correlationId, message);
    }
}
