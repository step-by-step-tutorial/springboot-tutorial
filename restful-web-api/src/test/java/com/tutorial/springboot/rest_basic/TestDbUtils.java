package com.tutorial.springboot.rest_basic;

import com.tutorial.springboot.rest_basic.dao.SampleRepository;
import com.tutorial.springboot.rest_basic.dto.SampleDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public final class TestDbUtils {

    public TestDbUtils() {
    }

    public static final class SampleCollection {

        public static void populate() {
            SampleRepository.insert(SampleDto.builder()
                    .text("test text")
                    .code(1)
                    .datetime(LocalDateTime.now())
                    .build());

            SampleRepository.insert(SampleDto.builder()
                    .text("test")
                    .code(2)
                    .datetime(LocalDateTime.now())
                    .build());
        }

        public static void truncate() {
            SampleRepository.truncate();
        }

        public static Set<Long> retrieveAllIds() {
            return SampleRepository.projectAllIds();
        }

        public static SampleDto retrieveById(Long id) {
            return SampleRepository.selectById(id);
        }

        public static List<SampleDto> retrieveAll() {
            return SampleRepository.selectAll();
        }

        public static Long retrieveId() {
            return TestDbUtils.SampleCollection.retrieveAllIds().stream().findFirst().orElseThrow();
        }

        public static SampleDto createSampleDto() {
            return SampleDto.builder().text("fake").code(1).datetime(LocalDateTime.now()).build();
        }
    }
}
