package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.test_utils.stub.EntityStubFactory;
import com.tutorial.springboot.rbac.test_utils.assistant.TestDatabaseAssistant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
public class UserRepositoryTest {

    @Autowired
    UserRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    class CreateTest {

        @Test
        void givenValidEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = EntityStubFactory.createUser(1, 1, 1).asOne();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getPassword().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
            assertTrue(actual.isEnabled());
        }

        @Test
        void givenValidEntities_whenSaveAll_thenReturnListOfPersistedEntity() {
            var givenEntities = EntityStubFactory.createUser(2, 1, 1).asList();

            var actual = systemUnderTest.saveAll(givenEntities);

            assertNotNull(actual);
            assertEquals(2, actual.size());
            assertTrue(actual.stream().allMatch(user -> user.getId() != null));
        }

        @Test
        void givenUserWithRoleAndPermission_whenSave_thenReturnPersistedEntity() {
            var givenEntity = EntityStubFactory.createUser(1, 1, 1).asOne();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getPassword().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
            assertTrue(actual.isEnabled());
            assertThat(actual.getAuthorities().stream().map(GrantedAuthority::getAuthority)).isNotEmpty();
            assertThat(actual.getPermissions()).isNotEmpty();
        }

    }

    @Nested
    class ReadTest {

        @Test
        void givenID_whenFindById_thenReturnEntity() {
            var givenId = testDatabaseAssistant.insertTestUser().asEntity.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getUsername().isEmpty());
            assertFalse(actual.get().getPassword().isEmpty());
            assertFalse(actual.get().getEmail().isEmpty());
            assertTrue(actual.get().isEnabled());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedEntity_whenUpdate_thenJustRunSuccessful() {
            var givenEntity = testDatabaseAssistant.insertTestUser().asEntity
                    .setUsername("newusername")
                    .setPassword("newpassword")
                    .setEmail("newusername@example.com")
                    .setEnabled(false);

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("newusername", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("newusername@example.com", actual.getEmail());
            assertFalse(actual.isEnabled());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestUser().asEntity.getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}