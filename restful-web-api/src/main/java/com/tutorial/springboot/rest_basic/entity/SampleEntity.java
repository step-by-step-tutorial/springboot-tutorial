package com.tutorial.springboot.rest_basic.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Objects;

public class SampleEntity implements Entity<Long> {

    private Long id;

    @NotBlank(message = "sample.text should not be null or empty")
    String text;

    @NotNull(message = "sample.code should not be null")
    Integer code;

    @NotNull(message = "sample.datetime should not be null")
    LocalDateTime datetime;

    private Integer version = 0;

    public SampleEntity() {
    }

    public SampleEntity(Long id) {
        this.id = id;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public SampleEntity id(Long id) {
        this.id = id;
        return this;
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
    public Integer version() {
        return version;
    }

    @Override
    public SampleEntity increaseVersion() {
        this.version++;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SampleEntity that = (SampleEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version);
    }

    @Override
    public String toString() {
        return """
                SampleEntity{
                     id=%d,
                     text='%s',
                     code=%d,
                     datetime='%s',
                     version='%s'
                }
                """.formatted(id, text, code, datetime, version);
    }
}

