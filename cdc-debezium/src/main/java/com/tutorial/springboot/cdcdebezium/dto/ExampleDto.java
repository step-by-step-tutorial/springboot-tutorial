package com.tutorial.springboot.cdcdebezium.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;

public record ExampleDto(
    String id,
    String name,
    int code,
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime datetime
) {
    @JsonIgnore
    public static ExampleDto create() {
        return new ExampleDto(null, null, 0, null);
    }

    @Override
    public String toString() {
        return "Example DTO: {" + "id:" + id + ", username:" + name + ", code:" + code + ", date:" + datetime + "}";
    }
}
