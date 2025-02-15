package com.tutorial.springboot.messagingpulsar.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.messagingpulsar.config.BrokerConstance.SUBSCRIPTION_NAME;
import static com.tutorial.springboot.messagingpulsar.config.BrokerConstance.TOPIC_NAME;

@Service
public class MessageListener {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @PulsarListener(
            subscriptionName = SUBSCRIPTION_NAME,
            topics = TOPIC_NAME
    )
    void listen(String message) {
        logger.info("Message Received: {}", message);
    }
}
