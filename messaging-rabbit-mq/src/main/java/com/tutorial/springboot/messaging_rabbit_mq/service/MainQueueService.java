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

    private final String destination;
    private final RabbitTemplate rabbitTemplate;

    public MainQueueService(
            @Value("${destination.main-queue}")
            final String destination,
            final RabbitTemplate rabbitTemplate) {
        this.destination = destination;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void push(Object model) {
        requireNonNull(model, "model should not be null");
        rabbitTemplate.send(destination, requireNonNull(createMessage(model), "message should not be null"));
        logger.info("message sent to {}: {}", destination, model);
    }
}
