package com.tutorial.springboot.rest_basic.dao;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class SampleRepository {

    public static final Map<Long, SampleDto> SAMPLE_TABLE = Collections.synchronizedMap(new HashMap<>());

    public static final AtomicLong SAMPLE_ID_GENERATOR = new AtomicLong();

    public static long insert(SampleDto dto) {
        final var id = SAMPLE_ID_GENERATOR.incrementAndGet();
        SAMPLE_TABLE.putIfAbsent(
                id,
                SampleDto.builder()
                        .from(dto)
                        .id(id)
                        .build()
        );
        return id;
    }

    public static List<SampleDto> selectAll() {
        return SAMPLE_TABLE.values()
                .stream()
                .toList();
    }

    public static SampleDto selectById(Long id) {
        return SampleRepository.SAMPLE_TABLE.get(id);
    }

    public static void update(Long id, SampleDto dto) {
        SAMPLE_TABLE.put(id, dto);
    }

    public static void delete(Long id) {
        SAMPLE_TABLE.remove(id);
    }

    public static void truncate() {
        SampleRepository.SAMPLE_TABLE.clear();
    }

    public static Set<Long> projectAllIds() {
        return SampleRepository.SAMPLE_TABLE.keySet();
    }
}
