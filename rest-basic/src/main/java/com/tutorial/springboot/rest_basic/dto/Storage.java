package com.tutorial.springboot.rest_basic.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Storage {

    public static final Map<Long, SampleDto> SAMPLE_COLLECTION = Collections.synchronizedMap(new HashMap<>());

    public static final AtomicLong SAMPLE_ID_GENERATOR = new AtomicLong();

    public static long save(SampleDto dto) {
        final var id = SAMPLE_ID_GENERATOR.incrementAndGet();
        SAMPLE_COLLECTION.putIfAbsent(
                id,
                SampleDto.builder()
                        .from(dto)
                        .id(id)
                        .build()
        );
        return id;
    }

    public static void update(Long id, SampleDto dto) {
        SAMPLE_COLLECTION.put(id, dto);
    }
}
