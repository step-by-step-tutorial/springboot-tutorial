package com.tutorial.springboot.messaging_rabbit_mq.listener;

import com.tutorial.springboot.messaging_rabbit_mq.model.MessageModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messaging_rabbit_mq.utils.MessageUtils.extractBody;

@Component
public class MainQueueListener {

    private final Logger logger = LoggerFactory.getLogger(MainQueueListener.class);

    @RabbitListener(queues = "${destination.main-queue}")
    public void processMessage(
            Message message,
            @Header(AmqpHeaders.CORRELATION_ID)
            String correlationId
    ) {
        logger.info("message received: {}, {}", extractBody(message, MessageModel.class), correlationId);
    }
}
