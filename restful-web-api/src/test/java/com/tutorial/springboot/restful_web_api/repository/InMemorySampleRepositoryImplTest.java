package com.tutorial.springboot.restful_web_api.repository;

import com.tutorial.springboot.restful_web_api.TestFixture;
import com.tutorial.springboot.restful_web_api.entity.SampleEntity;
import com.tutorial.springboot.restful_web_api.exception.ValidationException;
import com.tutorial.springboot.restful_web_api.transformer.SampleTransformer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static com.tutorial.springboot.restful_web_api.TestFixture.multiSample;
import static com.tutorial.springboot.restful_web_api.TestFixture.oneSample;
import static com.tutorial.springboot.restful_web_api.transformer.SampleTransformer.toEntities;
import static com.tutorial.springboot.restful_web_api.transformer.SampleTransformer.toEntity;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class InMemorySampleRepositoryImplTest {

    @Autowired
    private SampleRepository<Long, SampleEntity> systemUnderTest;

    @AfterEach
    void tearDown() {
        InMemoryDatabase.SAMPLE_TABLE.clear();
    }

    @Nested
    class InsertSampleTests {

        @Test
        void givenSample_whenInsert_thenReturnId() {
            var givenSample = toEntity(oneSample());
            var actual = assertDoesNotThrow(() -> systemUnderTest.insert(givenSample));
            assertNotNull(actual);
        }

        @Test
        void givenListOfSample_whenInsertAll_thenReturnListOfId() {
            var givenSamples = Arrays.stream(multiSample())
                    .map(SampleTransformer::toEntity)
                    .toArray(SampleEntity[]::new);
            var actual = assertDoesNotThrow(() -> systemUnderTest.insertBatch(givenSamples));
            assertNotNull(actual);
            assertFalse(actual.isEmpty());
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
    class SelectSampleTests {

        @Test
        void givenId_whenSelectById_thenReturnSample() {
            var preparedEntity = toEntity(oneSample());
            var givenId = systemUnderTest.insert(preparedEntity).orElseThrow();
            var actual = systemUnderTest.selectById(givenId).orElseThrow();
            assertNotNull(actual);
        }

        @Test
        void givenNothing_whenSelectAll_thenReturnListOfSample() {
            var givenEntities = toEntities(multiSample());
            systemUnderTest.insertBatch(givenEntities);
            var actual = systemUnderTest.selectAll();
            assertNotNull(actual);
            assertFalse(actual.toList().isEmpty());
        }

        @Test
        void givenListOfId_whenSelectAll_thenReturnListOfSample() {
            var givenEntities = toEntities(multiSample());
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
            var givenEntities = toEntities(multiSample());
            systemUnderTest.insertBatch(givenEntities);
            var actual = systemUnderTest.selectIdentifiers();
            assertNotNull(actual);
            assertFalse(actual.toList().isEmpty());
        }

        @Test
        void givenPageWhenSelectByPageThenReturnPagedSamples() {
            systemUnderTest.insertBatch(toEntities(multiSample()));

            var givenPage = 0;
            var givenSize = 2;

            var actual = systemUnderTest.selectByPage(givenPage, givenSize);

            assertNotNull(actual);
            assertTrue(actual.findAny().isPresent());
        }

        @Test
        void givenEmptyTableWhenCountThenReturnZero() {
            var actual = systemUnderTest.count();
            assertEquals(0, actual);
        }

        @Test
        void givenNonEmptyTableWhenCountThenReturnCorrectCount() {
            var expectedCount = systemUnderTest.insertBatch(toEntities(multiSample())).size();

            var actual = systemUnderTest.count();

            assertEquals(expectedCount, actual);
        }

    }

    @Nested
    class ExistsSampleTests {
        @Test
        void givenValidId_whenCheckExistence_thenReturnTrue() {
            var givenId = systemUnderTest.insert(toEntity(oneSample())).orElseThrow();
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
    class UpdateSampleTests {
        @Test
        void givenSampleAndId_whenUpdate_thenUpdatedSuccessfully() {
            var givenId = systemUnderTest.insert(toEntity(oneSample())).orElseThrow();
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
            var givenId = systemUnderTest.insert(toEntity(oneSample())).orElseThrow();
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
        void givenSampleWithDifferentIdAndId_whenUpdate_thenValidationException() {
            var givenId = systemUnderTest.insert(toEntity(oneSample())).orElseThrow();
            var givenSample = new SampleEntity()
                    .id(givenId + 1)
                    .code(TestFixture.randomCodeGenerator.nextInt())
                    .text("updated text")
                    .datetime(LocalDateTime.now());

            var actual = assertThrows(ValidationException.class, () -> systemUnderTest.update(givenId, givenSample));

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenInvalidId_whenUpdate_thenNoSuchElementException() {
            var givenId = -1L;
            var givenSample = toEntity(oneSample()).id(givenId);

            var actual = assertThrows(NoSuchElementException.class, () -> systemUnderTest.update(givenId, givenSample));

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

    }

    @Nested
    class DeleteSampleTests {
        @Test
        void givenId_whenDeleteById_thenDeletedSuccessfully() {
            var givenId = systemUnderTest.insert(toEntity(oneSample())).orElseThrow();
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.exists(givenId);
            });
            assertFalse(actual);
        }

        @Test
        void givenIdentifiers_whenDeleteAllById_thenAllDeletedSuccessfully() {
            var givenEntities = toEntities(multiSample());
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
            var givenEntities = toEntities(multiSample());
            systemUnderTest.insertBatch(givenEntities);
            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteAll();
                return systemUnderTest.selectAll();
            });
            assertEquals(0, actual.toList().size());
        }
    }
}