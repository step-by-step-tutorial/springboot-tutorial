package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.fixture.EntityFixture;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.rbac.fixture.EntityFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
@DisplayName("Tests for CRUD operations of UserRepository")
public class UserRepositoryTest {

    @Autowired
    UserRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;


    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        @Test
        @DisplayName("Saving a valid User entity should persist it and generate an ID.")
        void givenValidEntity_whenSave_thenReturnID() {
            var givenEntity = createTestUser();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(TEST_USER_USERNAME, actual.getUsername());
            assertEquals(TEST_USER_PASSWORD, actual.getPassword());
            assertEquals(TEST_USER_EMAIL, actual.getEmail());
            assertTrue(actual.isEnabled());
        }

        @Test
        @DisplayName("Saving multiple User entities should persist all and return correct count.")
        void givenValidEntities_whenSaveAll_thenReturnSavedEntities() {
            var users = EntityFixture.createMultipleTestUser(3);

            var actual = systemUnderTest.saveAll(users);

            assertNotNull(actual);
            assertEquals(3, actual.size());
            assertTrue(actual.stream().allMatch(user -> user.getId() != null));
        }

        @Test
        @DisplayName("Saving a User entity with Role and Permission should persist correctly.")
        void givenUserIncludeRoleAndPermission_whenSave_thenReturnEntityIncludeId() {
            var givenUser = createTestUser()
                    .addRole(createTestRole().addPermissions(createTestPermission()));

            var actual = systemUnderTest.save(givenUser);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(TEST_USER_USERNAME, actual.getUsername());
            assertEquals(TEST_USER_PASSWORD, actual.getPassword());
            assertEquals(TEST_USER_EMAIL, actual.getEmail());
            assertTrue(actual.isEnabled());
            assertThat(actual.getAuthorities().stream().map(GrantedAuthority::getAuthority)).containsOnly(TEST_ROLE_NAME);
            assertThat(actual.getPermissions()).containsOnly(TEST_PERMISSION_NAME);

        }

    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        @Test
        @DisplayName("Retrieving a User entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnEntity() {
            var givenId = testDatabaseAssistant.newTestUser().asEntity.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertEquals(TEST_USER_USERNAME, actual.get().getUsername());
            assertEquals(TEST_USER_PASSWORD, actual.get().getPassword());
            assertEquals(TEST_USER_EMAIL, actual.get().getEmail());
            assertTrue(actual.get().isEnabled());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        @Test
        @DisplayName("Updating a User entity should modify its properties.")
        void givenUpdatedEntity_whenUpdate_thenEntityIsUpdated() {
            var givenEntity = testDatabaseAssistant.newTestUser().asEntity
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
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        @Test
        @DisplayName("Deleting a User entity by ID should remove it from the repository.")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            var givenId = testDatabaseAssistant.newTestUser().asEntity.getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}