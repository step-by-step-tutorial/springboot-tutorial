package com.tutorial.springboot.messagingpulsar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.tutorial.springboot.messagingpulsar.config.BrokerConstance.TOPIC_NAME;

@Service
public class MessageService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PulsarTemplate<String> pulsarTemplate;

    public MessageService(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void send(String message) {
        Objects.requireNonNull(message);
        if (message.isBlank()) {
            logger.error("Message should not be empty.");
            throw new IllegalArgumentException("Message should not be empty.");
        }

        pulsarTemplate.send(TOPIC_NAME, message);
        logger.info("Message sent: {}", message);
    }
}
