package com.tutorial.springboot.rest_basic;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.dto.Storage;

import java.time.LocalDateTime;
import java.util.Set;

public final class TestDbUtils {

    public TestDbUtils() {
    }

    public static final class SampleCollection {

        public static void populate() {
          Storage.save(SampleDto.builder()
                  .text("test text")
                  .code(1)
                  .datetime(LocalDateTime.now())
                  .build());

           Storage.save(SampleDto.builder()
                  .text("test")
                  .code(2)
                  .datetime(LocalDateTime.now())
                  .build());

        }

        public static void truncate() {
            Storage.SAMPLE_COLLECTION.clear();
        }

        public static SampleDto select(Long id) {
            return Storage.SAMPLE_COLLECTION.get(id);
        }

        public static Set<Long> selectAll() {
            return Storage.SAMPLE_COLLECTION.keySet();
        }
    }
}
