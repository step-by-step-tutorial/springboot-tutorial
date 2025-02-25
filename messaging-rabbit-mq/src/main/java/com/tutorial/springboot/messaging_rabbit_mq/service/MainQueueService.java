package com.tutorial.springboot.messaging_rabbit_mq.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messaging_rabbit_mq.utils.MessageUtils.createMessage;
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueService {

    private final Logger logger = LoggerFactory.getLogger(MainQueueService.class);

    private final String queue;

    private final RabbitTemplate rabbitTemplate;

    public MainQueueService(@Value("${queue.main}") final String queue, final RabbitTemplate rabbitTemplate) {
        this.queue = queue;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(Object model) {
        requireNonNull(model, "Model should not be null");
        rabbitTemplate.send(queue, requireNonNull(createMessage(model), "message should not be null"));
        logger.info("Message sent to {}: {}", queue, model);
    }
}
