package com.tutorial.springboot.messagingpulsar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.annotation.PulsarListener;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.messagingpulsar.MessengerConstance.SUBSCRIPTION_NAME;
import static com.tutorial.springboot.messagingpulsar.MessengerConstance.TOPIC_NAME;

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
