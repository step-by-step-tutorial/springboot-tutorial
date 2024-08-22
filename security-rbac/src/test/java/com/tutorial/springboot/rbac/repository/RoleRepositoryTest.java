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
public class RoleRepositoryTest {

    @Autowired
    RoleRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;


    @Nested
    class CreateTest {

        @Test
        void givenValidEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = EntityStubFactory.createRole(1, 1).asOne();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenValidEntities_whenSaveAll_thenReturnListOfPersistedEntity() {
            var givenEntities = EntityStubFactory.createRole(2, 1).asList();

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
            var givenId = testDatabaseAssistant.insertTestRole().asEntity.getId();

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
            var givenEntity = testDatabaseAssistant.insertTestRole()
                    .asEntity
                    .setName("ROLE_UPDATED");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("ROLE_UPDATED", actual.getName());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestRole().asEntity.getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}