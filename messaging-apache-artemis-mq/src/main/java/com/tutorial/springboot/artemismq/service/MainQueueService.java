package com.tutorial.springboot.artemismq.service;

import com.tutorial.springboot.artemismq.model.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.artemismq.utils.MessageUtils.createSerializableMessage;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueService {

    private final Logger logger = LoggerFactory.getLogger(MainQueueService.class);

    private final String queue;

    private final JmsTemplate jmsTemplate;

    public MainQueueService(@Value("${queue.main}") final String queue, final JmsTemplate jmsTemplate) {
        this.queue = queue;
        this.jmsTemplate = jmsTemplate;
    }

    public void push(MessageModel model) {
        requireNonNull(model, "MessageModel should not be null");
        jmsTemplate.send(queue, createSerializableMessage(model));
        logger.info("Message sent to {}: {}", queue, model);
    }

}
