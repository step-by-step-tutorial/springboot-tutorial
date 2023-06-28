package com.tutorial.springboot.messagingactivemq.utils;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.ObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.MessageCreator;

import java.io.Serializable;
import java.util.Optional;
import java.util.UUID;

public final class MessageUtils {
    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    private MessageUtils() {
    }

    public static MessageCreator createSerializableMessage(Serializable message) {
        return session -> {
            ObjectMessage jmsMessage = session.createObjectMessage();
            jmsMessage.setObject(message);
            jmsMessage.setJMSCorrelationID(UUID.randomUUID().toString());
            return jmsMessage;
        };
    }

    public static <T> Optional<T> extractBody(Message message, Class<T> type) {
        try {
            var body = message.getBody(type);
            logger.info("converting message body succeeded: {}", body);
            return Optional.ofNullable(body);
        } catch (JMSException e) {
            logger.error("converting message body failed due to: {}", e.getMessage());
            return Optional.empty();
        }
    }

    public static String extractCorrelationId(Message message) {
        try {
            return message.getJMSCorrelationID();
        } catch (JMSException e) {
            logger.error("correlation Id does not exist for the reason that: {}", e.getMessage());
            return null;
        }
    }

    public static String extractDestination(Message message) {
        try {
            return message.getJMSDestination().toString();
        } catch (JMSException e) {
            logger.error("destination does not exist for the reason that: {}", e.getMessage());
            return null;
        }
    }

}
