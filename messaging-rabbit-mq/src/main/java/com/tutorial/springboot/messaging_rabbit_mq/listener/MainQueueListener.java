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
import static java.util.Objects.requireNonNull;

@Component
public class MainQueueListener {

    private final Logger logger = LoggerFactory.getLogger(MainQueueListener.class);

    @RabbitListener(queues = "${queue.main}")
    public void onMessage(final Message message, @Header(AmqpHeaders.CORRELATION_ID) final String correlationId) {
        requireNonNull(message, "Message should not be null");
        requireNonNull(correlationId, "Correlation Id should not be null");

        logger.info("Message received: {}, {}", extractBody(message, MessageModel.class), correlationId);
    }
}
