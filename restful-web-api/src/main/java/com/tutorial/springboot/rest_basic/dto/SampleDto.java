package com.tutorial.springboot.rest_basic.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record SampleDto(

        Long id,

        @NotBlank(message = "sample.text should not be null or empty")
        String text,

        @NotNull(message = "sample.code should not be null")
        Integer code,

        @NotNull(message = "sample.datetime should not be null")
        LocalDateTime datetime,

        Integer version
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String text;
        private Integer code;
        private LocalDateTime datetime;
        private Integer version;

        public Builder() {
        }

        public Builder from(SampleDto dto) {
            this.id = dto.id;
            this.text = dto.text;
            this.code = dto.code;
            this.datetime = dto.datetime;
            this.version = dto.version;
            return this;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder text(String text) {
            this.text = text;
            return this;
        }

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }

        public Builder datetime(LocalDateTime datetime) {
            this.datetime = datetime;
            return this;
        }

        public Builder version(Integer version) {
            this.version = version;
            return this;
        }

        public SampleDto build() {
            return new SampleDto(id, text, code, datetime, version);
        }
    }
}
