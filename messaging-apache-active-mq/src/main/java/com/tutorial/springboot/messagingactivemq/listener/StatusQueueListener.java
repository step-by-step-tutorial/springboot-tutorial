package com.tutorial.springboot.messagingactivemq.listener;

import com.tutorial.springboot.messagingactivemq.message.Status;
import jakarta.jms.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.messagingactivemq.utils.MessageUtils.extractBody;
import static java.util.Objects.requireNonNull;

@Component
public class StatusQueueListener {

    private final Logger logger = LoggerFactory.getLogger(StatusQueueListener.class);

    private final String destination;

    public StatusQueueListener(@Value("${queue.status-queue}") String destination) {
        this.destination = destination;
    }

    @JmsListener(destination = "${queue.status-queue}")
    public void onMessage(Message message) {
        requireNonNull(message);
        logger.info("status received from {}", destination);
        extractBody(message, Status.class)
                .ifPresentOrElse(
                        body -> logger.info("status processing succeeded: {}", body),
                        () -> logger.info("status processing failed"));
    }

}
