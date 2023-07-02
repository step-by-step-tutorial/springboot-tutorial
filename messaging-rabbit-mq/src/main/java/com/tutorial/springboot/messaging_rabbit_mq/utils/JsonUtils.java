package com.tutorial.springboot.messaging_rabbit_mq.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public final class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> byte[] toJsonByte(final T object) {
        try {
            return MAPPER.writeValueAsBytes(object);
        } catch (JsonProcessingException e) {
            logger.error("converting object to JSON byte failed due to: {}", e.getMessage());
            return new byte[0];
        }
    }

    public static <T> Optional<T> toModel(final byte[] jsonBytes, final Class<T> typeOfBody) {
        try {
            var body = MAPPER.readValue(jsonBytes, typeOfBody);
            logger.info("converting json bytes succeeded: {}", body);
            return Optional.ofNullable(body);
        } catch (Exception e) {
            logger.info("converting json bytes failed due to: {}", e.getMessage());
            return Optional.empty();
        }
    }
}
