package com.tutorial.springboot.messagingactivemq.service;

import com.tutorial.springboot.messagingactivemq.message.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueClient {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueClient.class);

    private final String destination;

    private final JmsTemplate jmsTemplate;

    public StatusQueueClient(
            @Value("${queue.status-queue}")
            String destination,
            JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.jmsTemplate = jmsTemplate;
    }

    public void push(Status message) {
        requireNonNull(message);
        jmsTemplate.send(destination, createSerializableMessage(message));
        logger.info("ack sent to {}: {}", destination, message);
    }
}
