package com.tutorial.springboot.h2;

import com.tutorial.springboot.h2.entity.SampleEntity;
import com.tutorial.springboot.h2.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@DisplayName("rdbms-h2: {@link SampleRepository} unit tests")
class SampleRepositoryTest {

    @Autowired
    SampleRepository underTest;

    /**
     * This class includes Stubs data.
     */
    static class Stub {
        static SampleEntity SAMPLE_ENTITY = SampleEntity.create()
                .setName("test")
                .setCode(1)
                .setDatetime(LocalDateTime.now());
    }

    @Nested
    @DisplayName("save nested tests")
    class SaveTest {
        @Test
        @DisplayName("save an entity")
        void GivenEntity_WhenInvokeSaveMethod_ThenReturnsPersistedEntity() {
            var givenEntity = Stub.SAMPLE_ENTITY;

            var actual = underTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(1L, actual.getId());
            assertEquals(Stub.SAMPLE_ENTITY.getName(), actual.getName());
            assertEquals(Stub.SAMPLE_ENTITY.getCode(), actual.getCode());
            assertEquals(Stub.SAMPLE_ENTITY.getDatetime(), actual.getDatetime());
        }
    }

    @Nested
    @DisplayName("find nested tests")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("find one entity by ID")
        void GivenId_WhenInvokeFindByIdMethod_ThenReturnsPersistedEntity() {

            var givenId = 1L;

            var actual = underTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(entity -> {
                assertEquals(Stub.SAMPLE_ENTITY.getName(), entity.getName());
                assertEquals(Stub.SAMPLE_ENTITY.getCode(), entity.getCode());
                assertEquals(Stub.SAMPLE_ENTITY.getDatetime(), entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("update nested tests")
    class UpdateTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("update one entity by new values")
        void GivenEntity_WhenTransactionIsClosedMethod_ThenTupleWillBeUpdated() {
            var givenId = 1L;
            var givenName = "updated_test";
            var givenCode = 2;

            var actual = underTest.findById(givenId);
            actual.ifPresent(entity -> {
                entity.setName(givenName);
                entity.setCode(givenCode);
            });

            assertTrue(true);
        }

        @AfterEach
        void afterUpdate() {
            var givenId = 1L;

            var actual = underTest.findById(givenId);
            assertTrue(actual.isPresent());
            actual.ifPresent(entity -> {
                assertEquals("updated_test", entity.getName());
                assertEquals(2, entity.getCode());
                assertEquals(Stub.SAMPLE_ENTITY.getDatetime(), entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("delete nested tests")
    class DeleteTest {

        @BeforeEach
        void initDatabase() {
            underTest.save(Stub.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("delete one entity by ID")
        void GivenId_WhenInvokeDeleteByIdMethod_ThenTupleWillBeDeletedFromDatabase() {
            var givenId = 1L;

            underTest.deleteById(givenId);

            var afterDelete = underTest.findById(givenId);
            assertTrue(afterDelete.isEmpty());
        }

    }
}
