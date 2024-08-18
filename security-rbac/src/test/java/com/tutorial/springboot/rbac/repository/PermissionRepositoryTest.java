package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.fixture.EntityFixture;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import org.junit.jupiter.api.DisplayName;
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
@DisplayName("Tests for CRUD operations of PermissionRepository")
public class PermissionRepositoryTest {

    @Autowired
    PermissionRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;


    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        @Test
        @DisplayName("Saving a valid Permission entity should persist it and return an entity include generated ID")
        void givenValidEntity_whenSave_thenReturnID() {
            var givenEntity = EntityFixture.createTestPermission();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(EntityFixture.TEST_PERMISSION_NAME, actual.getName());
        }

        @Test
        @DisplayName("Saving multiple Permission entities should persist all and return list of entities include generated ID")
        void givenValidEntities_whenSaveAll_thenReturnSavedEntities() {
            var entities = EntityFixture.createMultiTestPermission(3);

            var actual = systemUnderTest.saveAll(entities);

            assertNotNull(actual);
            assertEquals(3, actual.size());
            assertTrue(actual.stream().allMatch(entity -> entity.getId() != null));
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        @Test
        @DisplayName("Retrieving a Permission entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnEntity() {
            var givenId = testDatabaseAssistant.newTestPermission()
                    .asEntity
                    .getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertEquals(EntityFixture.TEST_PERMISSION_NAME, actual.get().getName());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        @Test
        @DisplayName("Updating a Permission entity should modify its properties")
        void givenUpdatedEntity_whenUpdate_thenEntityIsUpdated() {
            var givenEntity = testDatabaseAssistant.newTestPermission()
                    .asEntity
                    .setName("UPDATED");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("UPDATED", actual.getName());
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        @Test
        @DisplayName("Deleting a Permission entity by ID should remove it from the repository")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            var givenId = testDatabaseAssistant.newTestPermission()
                    .asEntity
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}
