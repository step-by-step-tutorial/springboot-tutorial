package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class SampleRepository {

    public static final Map<Long, SampleDto> OPERATIONS = Collections.synchronizedMap(new HashMap<>());

    public static final AtomicLong ID_GENERATOR = new AtomicLong();

    private SampleRepository() {
    }

}
