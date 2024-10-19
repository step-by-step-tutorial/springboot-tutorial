package com.tutorial.springboot.securityoauth2client.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Converter(autoApply = true)
public class JsonAttributeConverter<T> implements AttributeConverter<Map<String, Object>, String> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        if (attribute == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Could not convert attribute to JSON", e);
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String column) {
        if (column == null || column.isEmpty()) {
            return null;
        }
        try {
            return objectMapper.readValue(column, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (IOException e) {
            throw new IllegalArgumentException("Could not convert JSON to attribute", e);
        }
    }

}