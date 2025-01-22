package com.tutorial.springboot.cdcembeddeddebezium.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.Objects;

public class ExampleDto {

    private String id;

    private String name;

    private int code;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datetime;

    @JsonIgnore
    public static ExampleDto create() {
        return new ExampleDto();
    }

    public String getId() {
        return id;
    }

    public ExampleDto setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ExampleDto setName(String name) {
        this.name = name;
        return this;
    }

    public int getCode() {
        return code;
    }

    public ExampleDto setCode(int code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public ExampleDto setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExampleDto exampleDto = (ExampleDto) o;
        return Objects.equals(id, exampleDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Example DTO: {" + "id:" + id + ", username:" + name + ", code:" + code + ", date:" + datetime + "}";
    }
}
