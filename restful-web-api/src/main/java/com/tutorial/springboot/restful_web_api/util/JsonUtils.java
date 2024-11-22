package com.tutorial.springboot.restful_web_api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonUtils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonUtils() {
    }

    public static String toJsonString(Object clazz) {
        try {
            return OBJECT_MAPPER.writeValueAsString(clazz);
        } catch (JsonProcessingException e) {
            return clazz.toString();
        }
    }
}
