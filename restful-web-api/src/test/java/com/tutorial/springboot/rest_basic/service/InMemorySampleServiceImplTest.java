package com.tutorial.springboot.rest_basic.service;

import com.tutorial.springboot.rest_basic.TestFixture;
import com.tutorial.springboot.rest_basic.dto.SampleDto;
import com.tutorial.springboot.rest_basic.exception.ValidationException;
import com.tutorial.springboot.rest_basic.repository.InMemoryDatabase;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InMemorySampleServiceImplTest {

    @Autowired
    private SampleService<Long, SampleDto> systemUnderTest;

    @AfterEach
    void tearDown() {
        InMemoryDatabase.SAMPLE_TABLE.clear();
    }

    @Nested
    class SaveTests {

        @Test
        void givenSample_whenSave_thenReturnId() {
            var givenSample = TestFixture.oneSample();
            var actual = assertDoesNotThrow(() -> systemUnderTest.save(givenSample));
            assertNotNull(actual);
        }

        @Test
        void givenListOfSample_whenSaveAll_thenReturnListOfId() {
            var givenSamples = TestFixture.multiSample();
            var actual = assertDoesNotThrow(() -> systemUnderTest.batchSave(givenSamples));
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

        @Test
        void givenEmptyList_whenSaveAll_thenThrowIllegalStateException() {
            var givenSamples = new SampleDto[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.batchSave(givenSamples));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenSaveAll_thenThrowNullPointerException() {
            final SampleDto[] givenSamples = null;
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.batchSave(givenSamples));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindById_thenReturnSample() {
            var givenId = systemUnderTest.save(TestFixture.oneSample()).orElseThrow();
            var actual = systemUnderTest.findById(givenId).orElseThrow();
            assertNotNull(actual);
        }

        @Test
        void givenNothing_whenFindAll_thenReturnListOfSample() {
            systemUnderTest.batchSave(TestFixture.multiSample());

            var actual = systemUnderTest.selectAll();

            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

        @Test
        void givenListOfId_whenFindAll_thenReturnListOfSample() {
            var givenListOfId = systemUnderTest.batchSave(TestFixture.multiSample());
            var actual = systemUnderTest.findByIdentifiers(givenListOfId.toArray(Long[]::new));
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

        @Test
        void givenEmptyList_whenFindAll_thenThrowIllegalStateException() {
            var givenListOfId = new Long[0];

            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.findByIdentifiers(givenListOfId));

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenFindAll_thenThrowNullPointerException() {
            final Long[] givenListOfId = null;

            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.findByIdentifiers(givenListOfId));

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNothing_whenGetIdentifiers_thenReturnListOfId() {
            systemUnderTest.batchSave(TestFixture.multiSample());

            var actual = systemUnderTest.getIdentifiers();

            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

    }

    @Nested
    @DisplayName("check existence")
    class ExistsSampleTests {
        @Test
        void givenValidId_whenCheckExistence_thenReturnTrue() {
            var givenId = systemUnderTest.save(TestFixture.oneSample()).orElseThrow();
            var actual = systemUnderTest.exists(givenId);
            assertTrue(actual);
        }

        @Test
        void givenInvalidId_whenCheckExistence_thenReturnFalse() {
            var givenId = 0L;
            var actual = systemUnderTest.exists(givenId);
            assertFalse(actual);
        }
    }

    @Nested
    @DisplayName("update one")
    class UpdateSampleTests {
        @Test
        void givenSampleAndId_whenUpdate_thenUpdatedSuccessfully() {
            var givenId = systemUnderTest.save(TestFixture.oneSample()).orElseThrow();
            var givenSample = SampleDto.builder()
                    .id(givenId)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now())
                    .version(1)
                    .build();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenSample);
                return systemUnderTest.findById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals(SampleDto.builder().from(givenSample).version(2).build(), actual);
        }

        @Test
        void givenSampleWithoutIdAndId_whenUpdate_thenUpdatedSuccessfully() {
            var givenId = systemUnderTest.save(TestFixture.oneSample()).orElseThrow();
            var givenSample = SampleDto.builder()
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now())
                    .version(1)
                    .build();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenSample);
                return systemUnderTest.findById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals(givenSample.text(), actual.text());
            assertEquals(givenSample.code(), actual.code());
            assertEquals(givenSample.datetime(), actual.datetime());
        }

        @Test
        void givenSampleWithDifferentIdAndId_whenUpdate_thenIllegalStateException() {
            var givenId = systemUnderTest.save(TestFixture.oneSample()).orElseThrow();
            var givenSample = SampleDto.builder()
                    .id(givenId + 1)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now())
                    .build();

            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.update(givenId, givenSample));

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

    @Nested
    @DisplayName("delete one/all")
    class DeleteSampleTests {
        @Test
        void givenId_whenDeleteById_thenDeletedSuccessfully() {
            var givenId = systemUnderTest.save(TestFixture.oneSample()).orElseThrow();
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.exists(givenId);
            });
            assertFalse(actual);
        }

        @Test
        void givenIdentifiers_whenDeleteAllById_thenAllDeletedSuccessfully() {
            var givenIdentifiers = systemUnderTest.batchSave(TestFixture.multiSample()).toArray(Long[]::new);
            assertDoesNotThrow(() -> systemUnderTest.batchDelete(givenIdentifiers));
            Stream.of(givenIdentifiers)
                    .forEach(id -> assertFalse(systemUnderTest.exists(id)));
        }

        @Test
        void givenEmptyListOfId_whenDeleteAllByIdentifiers_thenThrowIllegalStateException() {
            var givenIdentifiers = new Long[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.batchDelete(givenIdentifiers));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenDeleteAllByIdentifiers_thenThrowNullPointerException() {
            final Long[] givenIdentifiers = null;
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.batchDelete(givenIdentifiers));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenSample_whenTruncate_thenAllDeletedSuccessfully() {
            systemUnderTest.batchSave(TestFixture.multiSample());
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteAll();
                return systemUnderTest.selectAll();
            });
            assertEquals(0, actual.size());
        }
    }
}