package com.tutorial.springboot.messagingpulsar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.pulsar.core.PulsarTemplate;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.messagingpulsar.MessengerConstance.TOPIC_NAME;

@Service
public class MessageSenderService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PulsarTemplate<String> pulsarTemplate;

    public MessageSenderService(PulsarTemplate<String> pulsarTemplate) {
        this.pulsarTemplate = pulsarTemplate;
    }

    public void send(String message) {
        logger.info("Message sent: {}", message);
        pulsarTemplate.send(TOPIC_NAME, message);
    }
}
