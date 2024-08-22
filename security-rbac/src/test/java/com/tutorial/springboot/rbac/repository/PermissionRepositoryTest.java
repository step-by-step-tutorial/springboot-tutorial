package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.test_utils.stub.EntityStubFactory;
import com.tutorial.springboot.rbac.test_utils.assistant.TestDatabaseAssistant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
public class PermissionRepositoryTest {

    @Autowired
    PermissionRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;


    @Nested
    class CreateTest {

        @Test
        void givenValidEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = EntityStubFactory.createPermission(1).asOne();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenValidEntities_whenSaveAll_thenReturnListOfPersistedEntity() {
            var givenEntities = EntityStubFactory.createPermission(2).asList();

            var actual = systemUnderTest.saveAll(givenEntities);

            assertNotNull(actual);
            assertEquals(2, actual.size());
            assertTrue(actual.stream().allMatch(entity -> entity.getId() != null));
        }
    }

    @Nested
    class ReadTest {

        @Test
        void givenId_whenFindById_thenReturnEntity() {
            var givenId = testDatabaseAssistant.insertTestPermission().asEntity.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getName().isEmpty());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedEntity_whenUpdate_thenJustRunSuccessful() {
            var givenEntity = testDatabaseAssistant.insertTestPermission()
                    .asEntity
                    .setName("UPDATED_PRIVILEGE");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("UPDATED_PRIVILEGE", actual.getName());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestPermission()
                    .asEntity
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}
