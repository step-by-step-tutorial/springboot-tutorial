package com.tutorial.springboot.restful_web_api.transformer;

import com.tutorial.springboot.restful_web_api.dto.SampleDto;
import com.tutorial.springboot.restful_web_api.entity.SampleEntity;

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
        return new SampleEntity()
                .id(dto.id())
                .code(dto.code())
                .text(dto.text())
                .datetime(dto.datetime())
                .version(dto.version());
    }

}
