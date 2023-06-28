package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.MessageHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueClient {

    private final Logger logger = LoggerFactory.getLogger(MainQueueClient.class);

    private final String destination;

    private final JmsTemplate jmsTemplate;

    public MainQueueClient(
            @Value("${queue.main-queue}")
            String destination,
            JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.jmsTemplate = jmsTemplate;
    }

    public void publish(MessageHolder message) {
        requireNonNull(message, "message should not be null");
        jmsTemplate.send(destination, createSerializableMessage(message));
        logger.info("message sent to {}: {}", destination, message);
    }

}
