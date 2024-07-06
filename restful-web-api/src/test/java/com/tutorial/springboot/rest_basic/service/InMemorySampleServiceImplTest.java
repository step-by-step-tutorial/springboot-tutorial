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

@DisplayName("Sample Service Unit tests")
@SpringBootTest
class InMemorySampleServiceImplTest {

    @Autowired
    private SampleService systemUnderTest;

    @AfterEach
    void tearDown() {
        InMemoryDatabase.SAMPLE_TABLE.clear();
    }

    @Nested
    @DisplayName("insert on/all")
    class InsertSampleTests {

        @Test
        void givenSample_whenInsert_thenReturnId() {
            var givenSample = TestFixture.oneSample();
            var actual = assertDoesNotThrow(() -> systemUnderTest.insert(givenSample));
            assertNotNull(actual);
        }

        @Test
        void givenListOfSample_whenInsertAll_thenReturnListOfId() {
            var givenSamples = TestFixture.multiSample();
            var actual = assertDoesNotThrow(() -> systemUnderTest.insertBatch(givenSamples));
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

        @Test
        void givenEmptyList_whenInsertAll_thenThrowIllegalStateException() {
            var givenSamples = new SampleDto[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.insertBatch(givenSamples));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenInsertAll_thenThrowNullPointerException() {
            final SampleDto[] givenSamples = null;
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.insertBatch(givenSamples));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

    }

    @Nested
    @DisplayName("select one/all")
    class SelectSampleTests {

        @Test
        void givenId_whenSelectById_thenReturnSample() {
            var givenId = systemUnderTest.insert(TestFixture.oneSample()).orElseThrow();
            var actual = systemUnderTest.selectById(givenId).orElseThrow();
            assertNotNull(actual);
        }

        @Test
        void givenNothing_whenSelectAll_thenReturnListOfSample() {
            systemUnderTest.insertBatch(TestFixture.multiSample());
            var actual = systemUnderTest.selectAll();
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

        @Test
        void givenListOfId_whenSelectAll_thenReturnListOfSample() {
            var givenListOfId = systemUnderTest.insertBatch(TestFixture.multiSample());
            var actual = systemUnderTest.selectBatch(givenListOfId.toArray(Long[]::new));
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

        @Test
        void givenEmptyList_whenSelectAll_thenThrowIllegalStateException() {
            var givenListOfId = new Long[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.selectBatch(givenListOfId));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenSelectAll_thenThrowNullPointerException() {
            final Long[] givenListOfId = null;
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.selectBatch(givenListOfId));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNothing_whenGetIdentities_thenReturnListOfId() {
            systemUnderTest.insertBatch(TestFixture.multiSample());
            var actual = systemUnderTest.selectIdentities();
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
        }

    }

    @Nested
    @DisplayName("check existence")
    class ExistsSampleTests {
        @Test
        void givenValidId_whenCheckExistence_thenReturnTrue() {
            var givenId = systemUnderTest.insert(TestFixture.oneSample()).orElseThrow();
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
            var givenId = systemUnderTest.insert(TestFixture.oneSample()).orElseThrow();
            var givenSample = SampleDto.builder()
                    .id(givenId)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now())
                    .version(1)
                    .build();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenSample);
                return systemUnderTest.selectById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals(SampleDto.builder().from(givenSample).version(2).build(), actual);
        }

        @Test
        void givenSampleWithoutIdAndId_whenUpdate_thenUpdatedSuccessfully() {
            var givenId = systemUnderTest.insert(TestFixture.oneSample()).orElseThrow();
            var givenSample = SampleDto.builder()
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now())
                    .version(1)
                    .build();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenSample);
                return systemUnderTest.selectById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals(givenSample.text(), actual.text());
            assertEquals(givenSample.code(), actual.code());
            assertEquals(givenSample.datetime(), actual.datetime());
        }

        @Test
        void givenSampleWithDifferentIdAndId_whenUpdate_thenIllegalStateException() {
            var givenId = systemUnderTest.insert(TestFixture.oneSample()).orElseThrow();
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
            var givenId = systemUnderTest.insert(TestFixture.oneSample()).orElseThrow();
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.exists(givenId);
            });
            assertFalse(actual);
        }

        @Test
        void givenIdentities_whenDeleteAllById_thenAllDeletedSuccessfully() {
            var givenIdentities = systemUnderTest.insertBatch(TestFixture.multiSample()).toArray(Long[]::new);
            assertDoesNotThrow(() -> systemUnderTest.deleteBatch(givenIdentities));
            Stream.of(givenIdentities)
                    .forEach(id -> assertFalse(systemUnderTest.exists(id)));
        }

        @Test
        void givenEmptyListOfId_whenDeleteAllByIdentities_thenThrowIllegalStateException() {
            var givenIdentities = new Long[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.deleteBatch(givenIdentities));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenDeleteAllByIdentities_thenThrowNullPointerException() {
            final Long[] givenIdentities = null;
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.deleteBatch(givenIdentities));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenSample_whenTruncate_thenAllDeletedSuccessfully() {
            systemUnderTest.insertBatch(TestFixture.multiSample());
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteAll();
                return systemUnderTest.selectAll();
            });
            assertEquals(0, actual.size());
        }
    }
}