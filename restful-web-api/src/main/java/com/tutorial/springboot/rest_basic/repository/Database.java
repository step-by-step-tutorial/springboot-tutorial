package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public final class Database {

    public static final Map<Long, SampleDto> SAMPLE_TABLE = Collections.synchronizedMap(new HashMap<>());

    public static final AtomicLong SAMPLE_ID_GENERATOR = new AtomicLong();

    private Database() {
    }

}
