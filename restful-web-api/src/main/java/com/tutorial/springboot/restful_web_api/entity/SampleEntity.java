package com.tutorial.springboot.restful_web_api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class SampleEntity extends Entity<Long, SampleEntity> {

    @NotBlank(message = "text should not be null or empty")
    private String text;

    @NotNull(message = "code should not be null")
    private Integer code;

    @NotNull(message = "datetime should not be null")
    private LocalDateTime datetime;

    public SampleEntity() {
    }

    public String text() {
        return text;
    }

    public SampleEntity text(String text) {
        this.text = text;
        return this;
    }

    public Integer code() {
        return code;
    }

    public SampleEntity code(Integer code) {
        this.code = code;
        return this;
    }

    public LocalDateTime datetime() {
        return datetime;
    }

    public SampleEntity datetime(LocalDateTime datetime) {
        this.datetime = datetime;
        return this;
    }

    @Override
    @JsonIgnore
    public SampleEntity updateFrom(SampleEntity newOne) {
        super.updateFrom(newOne);
        code(newOne.code());
        text(newOne.text());
        datetime(newOne.datetime());
        return this;
    }

}

