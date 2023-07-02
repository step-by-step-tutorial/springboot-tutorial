package com.tutorial.springboot.messaging_rabbit_mq.utils;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;

import java.util.Optional;
import java.util.UUID;

import static com.tutorial.springboot.messaging_rabbit_mq.utils.JsonUtils.toJsonByte;
import static com.tutorial.springboot.messaging_rabbit_mq.utils.JsonUtils.toModel;

public final class MessageUtils {

    private MessageUtils() {
    }

    public static Message createMessage(final Object model) {
        final var properties = new MessageProperties();
        properties.setCorrelationId(UUID.randomUUID().toString());
        return new Message(toJsonByte(model), properties);
    }

    public static <T> Optional<T> extractBody(final Message message, final Class<T> typeOfBody) {
        return toModel(message.getBody(), typeOfBody);
    }

}
