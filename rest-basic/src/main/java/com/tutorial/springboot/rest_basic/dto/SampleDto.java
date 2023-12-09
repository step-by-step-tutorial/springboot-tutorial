package com.tutorial.springboot.rest_basic.dto;

import java.time.LocalDateTime;

public record SampleDto(
        Long id,
        String text,
        Integer code,
        LocalDateTime datetime
) {
    public static class Builder {
        private Long id;
        private String text;
        private Integer code;
        private LocalDateTime datetime;

        public Builder() {
        }

        public Builder from(SampleDto dto) {
            this.id = dto.id;
            this.text = dto.text;
            this.code = dto.code;
            this.datetime = dto.datetime;
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

        public SampleDto build() {
            return new SampleDto(id, text, code, datetime);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
