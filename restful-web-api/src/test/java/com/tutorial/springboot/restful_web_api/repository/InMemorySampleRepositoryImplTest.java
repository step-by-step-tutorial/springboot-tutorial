package com.tutorial.springboot.restful_web_api.repository;

import com.tutorial.springboot.restful_web_api.TestFixture;
import com.tutorial.springboot.restful_web_api.entity.SampleEntity;
import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import com.tutorial.springboot.restful_web_api.transformer.SampleTransformer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Stream;

import static com.tutorial.springboot.restful_web_api.transformer.SampleTransformer.toEntity;
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
        void givenNothing_whenGetIdentifiers_thenReturnListOfId() {
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            systemUnderTest.insertBatch(givenEntities);
            var actual = systemUnderTest.selectIdentifiers();
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
            var givenSample = systemUnderTest.selectById(givenId).orElseThrow()
                    .id(givenId)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenSample);
                return systemUnderTest.selectById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            givenSample.increaseVersion();
            assertEquals(givenSample, actual);
        }

        @Test
        void givenSampleWithoutIdAndId_whenUpdate_thenUpdatedSuccessfully() {
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var givenSample = systemUnderTest.selectById(givenId).orElseThrow()
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

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
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var givenSample = new SampleEntity()
                    .id(givenId + 1)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

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
            var givenId = systemUnderTest.insert(toEntity(TestFixture.oneSample())).orElseThrow();
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.exists(givenId);
            });
            assertFalse(actual);
        }

        @Test
        void givenIdentifiers_whenDeleteAllById_thenAllDeletedSuccessfully() {
            var givenEntities = Arrays.stream(TestFixture.multiSample()).map(SampleTransformer::toEntity).toArray(SampleEntity[]::new);
            var givenIdentifiers = systemUnderTest.insertBatch(givenEntities).toArray(Long[]::new);
            assertDoesNotThrow(() -> systemUnderTest.deleteBatch(givenIdentifiers));
            Stream.of(givenIdentifiers)
                    .forEach(id -> assertFalse(systemUnderTest.exists(id)));
        }

        @Test
        void givenEmptyListOfId_whenDeleteAllByIdentifiers_thenThrowIllegalStateException() {
            var givenIdentifiers = new Long[0];
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.deleteBatch(givenIdentifiers));
            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenNullAsList_whenDeleteAllByIdentifiers_thenThrowNullPointerException() {
            final Long[] givenIdentifiers = null;
            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.deleteBatch(givenIdentifiers));
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