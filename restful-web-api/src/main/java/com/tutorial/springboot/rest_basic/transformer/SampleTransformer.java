package com.tutorial.springboot.rest_basic.transformer;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.entity.SampleEntity;

public final class SampleTransformer {

    private SampleTransformer() {
    }

    public static SampleDto toDto(SampleEntity entity) {
        return SampleDto.builder()
                .id(entity.id())
                .code(entity.code())
                .text(entity.text())
                .datetime(entity.datetime())
                .version(entity.version())
                .build();
    }

    public static SampleEntity toEntity(SampleDto dto) {
        return new SampleEntity(dto.id())
                .code(dto.code())
                .text(dto.text())
                .datetime(dto.datetime());
    }

}
