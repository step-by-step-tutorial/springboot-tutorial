package com.tutorial.springboot.artemismq.service;

import com.tutorial.springboot.artemismq.model.StatusModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.artemismq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueService {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueService.class);

    private final String queue;

    private final JmsTemplate jmsTemplate;

    public StatusQueueService(@Value("${queue.status}") final String queue, final JmsTemplate jmsTemplate) {
        this.queue = queue;
        this.jmsTemplate = jmsTemplate;
    }

    public void push(StatusModel model) {
        requireNonNull(model, "StatusModel should not be null");
        jmsTemplate.send(queue, createSerializableMessage(model));
        logger.info("Acknowledge sent to {}: {}", queue, model);
    }
}
