package com.tutorial.springboot.cloudawssqs.service;

import io.awspring.cloud.sqs.annotation.SqsListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
public class MessageListener {

    private final Logger logger = LoggerFactory.getLogger(MessageListener.class);

    @SqsListener("${app.queue.name}")
    public void receive(@Payload String message, @Header(name = "CorrelationId", required = false) String correlationId) {
        logger.info("Received message: Body={}, Correlation ID={}", message, correlationId);
    }
}