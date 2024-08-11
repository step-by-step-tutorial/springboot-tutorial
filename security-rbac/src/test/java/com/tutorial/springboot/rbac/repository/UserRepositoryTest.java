package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
@DisplayName("Tests for CRUD operationsof UserRepository")
public class UserRepositoryTest {

    @Autowired
    private UserRepository systemUnderTest;

    static class EntityFixture {
        public static User createUser(String username, String password, String email, boolean enabled) {
            return new User()
                    .setUsername(username)
                    .setPassword(password)
                    .setEmail(email)
                    .setEnabled(enabled);
        }

        public static List<User> createMultipleUsers(String... usernames) {
            return Arrays.stream(usernames)
                    .map(username -> createUser(username, "password123", username + "@example.com", true))
                    .toList();
        }
    }

    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        @Test
        @DisplayName("Saving a valid User entity should persist it and generate an ID.")
        void givenValidEntity_whenSave_thenReturnID() {
            var givenEntity = EntityFixture.createUser("testuser", "password123", "testuser@example.com", true);

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals("testuser", actual.getUsername());
            assertEquals("password123", actual.getPassword());
            assertEquals("testuser@example.com", actual.getEmail());
            assertTrue(actual.isEnabled());
        }

        @Test
        @DisplayName("Saving multiple User entities should persist all and return correct count.")
        void givenValidEntities_whenSaveAll_thenReturnSavedEntities() {
            var users = EntityFixture.createMultipleUsers("user1", "user2", "user3");

            var actual = systemUnderTest.saveAll(users);

            assertNotNull(actual);
            assertEquals(3, actual.size());
            assertTrue(actual.stream().allMatch(user -> user.getId() != null));
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        private Long userId;

        @BeforeEach
        void setUp() {
            var entity = systemUnderTest.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            userId = entity.getId();
        }

        @Test
        @DisplayName("Retrieving a User entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnEntity() {
            var actual = systemUnderTest.findById(userId);

            assertTrue(actual.isPresent());
            assertEquals("testuser", actual.get().getUsername());
            assertEquals("password123", actual.get().getPassword());
            assertEquals("testuser@example.com", actual.get().getEmail());
            assertTrue(actual.get().isEnabled());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        private Long userId;

        @BeforeEach
        void setUp() {
            var entity = systemUnderTest.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            userId = entity.getId();
        }

        @Test
        @DisplayName("Updating a User entity should modify its properties.")
        void givenUpdatedEntity_whenUpdate_thenEntityIsUpdated() {
            var user = systemUnderTest.findById(userId).get();
            user.setUsername("updateduser").setPassword("newpassword").setEmail("updated@example.com").setEnabled(false);

            var actual = systemUnderTest.save(user);

            assertEquals("updateduser", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("updated@example.com", actual.getEmail());
            assertFalse(actual.isEnabled());
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        private Long userId;

        @BeforeEach
        void setUp() {
            var entity = systemUnderTest.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            userId = entity.getId();
        }

        @Test
        @DisplayName("Deleting a User entity by ID should remove it from the repository.")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            systemUnderTest.deleteById(userId);

            var actual = systemUnderTest.findById(userId);

            assertFalse(actual.isPresent());
        }
    }
}
