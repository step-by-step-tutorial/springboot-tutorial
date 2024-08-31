package com.tutorial.springboot.restful_web_api.repository;

import com.tutorial.springboot.restful_web_api.entity.SampleEntity;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class InMemoryDatabase {

    public static final Map<Long, SampleEntity> SAMPLE_TABLE = Collections.synchronizedMap(new HashMap<>());

    public static final AtomicLong SAMPLE_ID_GENERATOR = new AtomicLong();

    private InMemoryDatabase() {
    }
}
