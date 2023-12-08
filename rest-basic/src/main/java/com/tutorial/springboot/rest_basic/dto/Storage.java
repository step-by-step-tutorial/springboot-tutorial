package com.tutorial.springboot.rest_basic.dto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class Storage {

    public static final Map<Long, SampleDto> SAMPLE_COLLECTION = Collections.synchronizedMap(new HashMap<>());

    public static final AtomicLong SAMPLE_ID_GENERATOR = new AtomicLong();

    public static long save(SampleDto dto) {
        final var id = Storage.SAMPLE_ID_GENERATOR.getAndIncrement();
        Storage.SAMPLE_COLLECTION.put(id, dto.toPersistable(id));
        return id;
    }
}
