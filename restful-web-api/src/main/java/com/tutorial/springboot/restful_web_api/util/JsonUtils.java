package com.tutorial.springboot.restful_web_api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static com.tutorial.springboot.restful_web_api.validation.ObjectValidation.shouldNotBeNull;

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
