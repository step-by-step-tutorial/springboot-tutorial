package com.tutorial.springboot.rest_basic.repository;

import com.tutorial.springboot.rest_basic.TestFixture;
import com.tutorial.springboot.rest_basic.entity.SampleEntity;
import com.tutorial.springboot.rest_basic.exception.ValidationException;
import com.tutorial.springboot.rest_basic.transformer.SampleTransformer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.tutorial.springboot.rest_basic.transformer.SampleTransformer.toEntity;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Sample Repository Unit tests")
@SpringBootTest
class InMemorySampleRepositoryImplTest {

    @Autowired
    private SampleRepository<Long, SampleEntity> systemUnderTest;

    @AfterEach
    void tearDown() {
        InMemoryDatabase.SAMPLE_TABLE.clear();
    }

    @Nested
    @DisplayName("insert on/all")
    class InsertSampleTests {

        @Test
        void givenSample_whenInsert_thenReturnId() {
            var givenSample = toEntity(TestFixture.oneSample());
            var actual = assertDoesNotThrow(() -> systemUnderTest.insert(givenSample));
            assertNotNull(actual);
        }

        @Test
        void givenListOfSample_whenInsertAll_thenReturnListOfId() {
            var givenSamples = Arrays.stream(TestFixture.multiSample())
                    .map(SampleTransformer::toEntity)
                    .toArray(SampleEntity[]::new);
            var actual = assertDoesNotThrow(() -> systemUnderTest.insertBatch(givenSamples));
            assertNotNull(actual);
            assertFalse(actual.toList().isEmpty());
        }

        @Test
        void givenEmptyList_whenInsertAll_thenThrowIllegalStateException() {
            var givenSamples = new SampleEntity[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.insertBatch(givenSamples));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenInsertAll_thenThrowNullPointerException() {
            final SampleEntity[] givenSamples = null;
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
            var preparedEntity = toEntity(TestFixture.oneSample());
            var givenId = systemUnderTest.insert(preparedEntity).orElseThrow();
            var actual = systemUnderTest.selectById(givenId).orElseThrow();
            assertNotNull(actual);
        }

        @Test
        void givenNothing_whenSelectAll_thenReturnListOfSample() {
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            systemUnderTest.insertBatch(givenEntities);
            var actual = systemUnderTest.selectAll();
            assertNotNull(actual);
            assertFalse(actual.toList().isEmpty());
        }

        @Test
        void givenListOfId_whenSelectAll_thenReturnListOfSample() {
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            var givenListOfId = systemUnderTest.insertBatch(givenEntities);
            var actual = systemUnderTest.selectBatch(givenListOfId.toArray(Long[]::new));
            assertNotNull(actual);
            assertFalse(actual.toList().isEmpty());
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
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            systemUnderTest.insertBatch(givenEntities);
            var actual = systemUnderTest.selectIdentities();
            assertNotNull(actual);
            assertFalse(actual.toList().isEmpty());
        }

    }

    @Nested
    @DisplayName("check existence")
    class ExistsSampleTests {
        @Test
        void givenValidId_whenCheckExistence_thenReturnTrue() {
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
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
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var givenSample = new SampleEntity(givenId)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenSample, givenId, 1);
                return systemUnderTest.selectById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            givenSample.increaseVersion();
            assertEquals(givenSample, actual);
        }

        @Test
        void givenSampleWithoutIdAndId_whenUpdate_thenUpdatedSuccessfully() {
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var givenSample = new SampleEntity()
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenSample, givenId, 1);
                return systemUnderTest.selectById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals(givenSample.text(), actual.text());
            assertEquals(givenSample.code(), actual.code());
            assertEquals(givenSample.datetime(), actual.datetime());
        }

        @Test
        void givenSampleWithDifferentIdAndId_whenUpdate_thenIllegalStateException() {
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var givenSample = new SampleEntity(givenId + 1)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.update(givenSample, givenId, 1));

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

    @Nested
    @DisplayName("delete one/all")
    class DeleteSampleTests {
        @Test
        void givenId_whenDeleteById_thenDeletedSuccessfully() {
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.exists(givenId);
            });
            assertFalse(actual);
        }

        @Test
        void givenIdentities_whenDeleteAllById_thenAllDeletedSuccessfully() {
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            var givenIdentities = systemUnderTest.insertBatch(givenEntities).toArray(Long[]::new);
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
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            systemUnderTest.insertBatch(givenEntities);
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteAll();
                return systemUnderTest.selectAll();
            });
            assertEquals(0, actual.toList().size());
        }
    }
}