package com.tutorial.springboot.h2;

import com.tutorial.springboot.h2.domain.SampleEntity;
import com.tutorial.springboot.h2.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("Sample Repository Tests")
class SampleRepositoryTest {

    @Autowired
    private SampleRepository underTest;

    static class Stub {
        static SampleEntity SAMPLE = SampleEntity.create()
                .setName("test")
                .setCode(1)
                .setDatetime(LocalDateTime.now());
    }

    @Nested
    @DisplayName("save nested tests")
    class SaveTest {
        @Test
        @DisplayName("save an entity")
        void givenEntity_whenInvokeSave_thenReturnsPersistedEntity() {
            var givenEntity = Stub.SAMPLE;

            var result = underTest.save(givenEntity);

            assertNotNull(result);
            assertEquals(1L, result.getId());
            assertEquals(Stub.SAMPLE.getName(), result.getName());
            assertEquals(Stub.SAMPLE.getCode(), result.getCode());
            assertEquals(Stub.SAMPLE.getDatetime(), result.getDatetime());
        }
    }

    @Nested
    @DisplayName("find nested tests")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE);
        }

        @Test
        @DisplayName("find one entity by ID")
        void givenId_whenInvokeFindById_thenReturnsPersistedEntity() {

            var givenId = 1L;

            var result = underTest.findById(givenId);

            assertTrue(result.isPresent());
            result.ifPresent(entity -> {
                assertEquals(Stub.SAMPLE.getName(), entity.getName());
                assertEquals(Stub.SAMPLE.getCode(), entity.getCode());
                assertEquals(Stub.SAMPLE.getDatetime(), entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("update nested tests")
    class UpdateTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE);
        }

        @Test
        @DisplayName("update one entity by new values")
        void givenEntity_whenTransactionIsClosed_thenTupleWillBeUpdated() {
            var givenId = 1L;
            var givenEntity = underTest.findById(givenId);

            givenEntity.ifPresent(entity -> {
                entity.setName("updated_test");
                entity.setCode(2);
            });

            assertTrue(true);
        }

        @AfterEach
        void assertionCheck() {
            var givenId = 1L;
            var result = underTest.findById(givenId);

            assertTrue(result.isPresent());
            result.ifPresent(entity -> {
                assertEquals("updated_test", entity.getName());
                assertEquals(2, entity.getCode());
                assertEquals(Stub.SAMPLE.getDatetime(), entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("delete nested tests")
    class DeleteTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE);
        }

        @Test
        @DisplayName("delete one entity by ID")
        void givenId_whenInvokeDeleteById_thenTupleWillBeDeletedFromDatabase() {
            var givenId = 1L;
            underTest.deleteById(givenId);
            assertTrue(true);
        }

        @AfterEach
        void assertionCheck() {
            var givenId = 1L;
            var result = underTest.findById(givenId);
            assertTrue(result.isEmpty());
        }
    }
}
