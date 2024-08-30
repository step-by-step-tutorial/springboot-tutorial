package com.tutorial.springboot.rest_basic.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

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

    public SampleEntity updateFrom(SampleEntity newOne) {
        super.updateFrom(newOne);
        code(newOne.code());
        text(newOne.text());
        datetime(newOne.datetime());
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (SampleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    @Override
    public String toString() {
        return """
                SampleEntity: {
                     id=%d,
                     text='%s',
                     code=%d,
                     datetime='%s',
                     version='%s'
                }
                """.formatted(id, text, code, datetime, version);
    }
}

