package com.tutorial.springboot.messaging_artemis_mq.service;

import com.tutorial.springboot.messaging_artemis_mq.model.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messaging_artemis_mq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueService {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueService.class);

    private final String destination;

    private final JmsTemplate jmsTemplate;

    public StatusQueueService(
            @Value("${destination.status-queue}")
            final String destination,
            final JmsTemplate jmsTemplate) {
        this.destination = destination;
        this.jmsTemplate = jmsTemplate;
    }

    public void push(StatusModel model) {
        requireNonNull(model, "model should not be null");
        jmsTemplate.send(destination, createSerializableMessage(model));
        logger.info("ack sent to {}: {}", destination, model);
    }
}
