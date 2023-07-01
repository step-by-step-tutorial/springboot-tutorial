package com.tutorial.springboot.messaging_rabbit_mq.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Optional;
import java.util.UUID;

public final class MessageUtils {

    private static final Logger logger = LoggerFactory.getLogger(MessageUtils.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private MessageUtils() {
    }

    public static Message createMessage(final Object model) {
        try {
            final var properties = new MessageProperties();
            properties.setCorrelationId(UUID.randomUUID().toString());
            return new Message(OBJECT_MAPPER.writeValueAsBytes(model), properties);
        } catch (Exception e) {
            logger.error("message creation failed due to: {}", e.getMessage());
            return null;
        }
    }

    public static <T> Optional<T> extractBody(final Message message, final Class<T> typeOfBody) {
        try {
            var body = OBJECT_MAPPER.readValue(message.getBody(), typeOfBody);
            logger.info("converting message body succeeded: {}", body);
            return Optional.ofNullable(body);
        } catch (Exception e) {
            logger.info("converting message body failed due to: {}", e.getMessage());
            return Optional.empty();
        }
    }

}
