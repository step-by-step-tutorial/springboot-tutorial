package com.tutorial.springboot.rest_basic.dto;

import java.time.LocalDateTime;

public record SampleDto(
        Long id,
        String text,
        int code,
        LocalDateTime datetime
) {
    public SampleDto toPersistable(long id){
        return new SampleDto(id, text, code, datetime);
    }
}
