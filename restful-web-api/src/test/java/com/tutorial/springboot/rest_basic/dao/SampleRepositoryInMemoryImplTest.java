package com.tutorial.springboot.rest_basic.dao;

import com.tutorial.springboot.rest_basic.dto.SampleDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SampleRepositoryInMemoryImplTest {

    private SampleRepository sampleRepositoryInMemoryImpl;

    static Random random = new Random();

    static SampleDto createSample() {
        return SampleDto.builder()
                .id(UUID.randomUUID().getMostSignificantBits())
                .text("test")
                .code(random.nextInt())
                .datetime(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    void setup() {
        sampleRepositoryInMemoryImpl = new SampleRepositoryInMemoryImpl();
    }

    @Test
    void testInsert() {
        var givenDto = createSample();
        var actual = sampleRepositoryInMemoryImpl.insert(givenDto);
        assertNotNull(actual, "Inserted successfully");
    }

    @Test
    void testInsertAll() {
        var givenItems = new SampleDto[]{createSample(), createSample()};
        var actual = sampleRepositoryInMemoryImpl.insertAll(givenItems);
        assertEquals(2, actual.size(), "Inserted all successfully");
    }

    @Test
    void testSelectById() {
        SampleDto sampleDto = createSample();
        long id = sampleRepositoryInMemoryImpl.insert(sampleDto).get();
        SampleDto dto = sampleRepositoryInMemoryImpl.selectById(id).get();
        assertNotNull(dto, "Selected successfully by id");
    }

    @Test
    void testSelectAll() {
        SampleDto sampleDto1 = createSample();
        SampleDto sampleDto2 = createSample();
        sampleRepositoryInMemoryImpl.insertAll(sampleDto1, sampleDto2);
        List<SampleDto> dtos = sampleRepositoryInMemoryImpl.selectAll();
        assertEquals(2, dtos.size(), "All selected successfully");
    }

    @Test
    void testProjectAllIds() {
        SampleDto sampleDto1 = createSample();
        SampleDto sampleDto2 = createSample();
        sampleRepositoryInMemoryImpl.insertAll(sampleDto1, sampleDto2);
        List<Long> ids = sampleRepositoryInMemoryImpl.identities();
        assertEquals(2, ids.size(), "All ids projected successfully");
    }

    @Test
    void testExists() {
        SampleDto sampleDto = createSample();
        long id = sampleRepositoryInMemoryImpl.insert(sampleDto).get();
        Assertions.assertTrue(sampleRepositoryInMemoryImpl.exists(id), "Exists successfully");
    }

    @Test
    void testUpdate() {
        SampleDto sampleDto = createSample();
        long id = sampleRepositoryInMemoryImpl.insert(sampleDto).get();
        sampleRepositoryInMemoryImpl.update(id, sampleDto);
        SampleDto updatedDto = sampleRepositoryInMemoryImpl.selectById(id).get();
        assertEquals(sampleDto, updatedDto, "Updated successfully");
    }

    @Test
    void testDeleteById() {
        SampleDto sampleDto = createSample();
        long id = sampleRepositoryInMemoryImpl.insert(sampleDto).get();
        sampleRepositoryInMemoryImpl.deleteById(id);
        Assertions.assertFalse(sampleRepositoryInMemoryImpl.exists(id), "Deleted successfully by id");
    }

    @Test
    void testDeleteAllById() {
        SampleDto sampleDto1 = createSample();
        SampleDto sampleDto2 = createSample();
        List<Long> ids = sampleRepositoryInMemoryImpl.insertAll(sampleDto1, sampleDto2);
        sampleRepositoryInMemoryImpl.deleteAll(ids.toArray(new Long[0]));
        Assertions.assertFalse(sampleRepositoryInMemoryImpl.exists(ids.get(0)) && sampleRepositoryInMemoryImpl.exists(ids.get(1)), "All deleted successfully by ids");
    }

    @Test
    void testTruncate() {
        SampleDto sampleDto = createSample();
        sampleRepositoryInMemoryImpl.insert(sampleDto);
        sampleRepositoryInMemoryImpl.deleteAll();
        List<SampleDto> dtos = sampleRepositoryInMemoryImpl.selectAll();
        assertEquals(0, dtos.size(), "All truncated successfully");
    }
}