package com.tutorial.springboot.streaming_kafka.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import static java.util.Objects.requireNonNull;

@Service
public class SinkTopicListener {

    private final Logger logger = LoggerFactory.getLogger(SinkTopicListener.class);

    @Value("${topic.sink}")
    private String topic;

    @KafkaListener(topics = "${topic.sink}")
    public void onMessage(
            @Payload String message,
            @Header(KafkaHeaders.CORRELATION_ID) String correlationId
    ) {
        requireNonNull(message, "Message should not be null");
        requireNonNull(correlationId, "Correlation Id should not be null");

        logger.info("Message received from {}: {}", topic, message);
    }
}
