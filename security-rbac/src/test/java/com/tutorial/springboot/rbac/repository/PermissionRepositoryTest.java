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
public class PermissionRepositoryTest {

    @Autowired
    PermissionRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;


    @Nested
    class CreateTest {

        @Test
        void givenValidEntity_whenSave_thenReturnPersistedEntity() {
            var givenEntity = EntityFixture.createTestPermission();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(EntityFixture.TEST_PERMISSION_NAME, actual.getName());
        }

        @Test
        void givenValidEntities_whenSaveAll_thenReturnPersistedEntities() {
            var entities = EntityFixture.createMultiTestPermission(3);

            var actual = systemUnderTest.saveAll(entities);

            assertNotNull(actual);
            assertEquals(3, actual.size());
            assertTrue(actual.stream().allMatch(entity -> entity.getId() != null));
        }
    }

    @Nested
    class ReadTest {

        @Test
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
    class UpdateTest {

        @Test
        void givenUpdatedEntity_whenUpdate_thenJustRunSuccessful() {
            var givenEntity = testDatabaseAssistant.newTestPermission()
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
        void givenID_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.newTestPermission()
                    .asEntity
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}
