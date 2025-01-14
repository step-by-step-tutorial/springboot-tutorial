package com.tutorial.springboot.cdcdebezium.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static <T> String toJson(T model) throws JsonProcessingException {
        return MAPPER.writeValueAsString(model);
    }

    public static <T> T toModel(String jsonString, Class<T> model) throws JsonProcessingException {
        return MAPPER.readValue(jsonString, model);
    }
}
