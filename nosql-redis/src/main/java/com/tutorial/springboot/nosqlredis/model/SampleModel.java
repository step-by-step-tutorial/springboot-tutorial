package com.tutorial.springboot.nosqlredis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.Objects;

public class SampleModel {

    private String id;

    private String name;

    private int code;

    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime datetime;

    @JsonIgnore
    public static SampleModel create() {
        return new SampleModel();
    }

    public String getId() {
        return id;
    }

    public SampleModel setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SampleModel setName(String name) {
        this.name = name;
        return this;
    }

    public int getCode() {
        return code;
    }

    public SampleModel setCode(int code) {
        this.code = code;
        return this;
    }

    public LocalDateTime getDatetime() {
        return datetime;
    }

    public SampleModel setDatetime(LocalDateTime datetime) {
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
        SampleModel sampleModel = (SampleModel) o;
        return Objects.equals(id, sampleModel.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Sample Model: {" + "id:" + id + ", username:" + name + ", code:" + code + ", date:" + datetime + "}";
    }
}
