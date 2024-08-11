package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.entity.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
@DisplayName("Tests for CRUD operations of UserRoleRepository")
public class UserRoleRepositoryTest {

    @Autowired
    private UserRoleRepository systemUnderTest;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    static class EntityFixture {
        public static User createUser(String username, String password, String email, boolean enabled) {
            return new User()
                    .setUsername(username)
                    .setPassword(password)
                    .setEmail(email)
                    .setEnabled(enabled);
        }

        public static Role createRole(String authority) {
            return new Role().setAuthority(authority);
        }

        public static UserRole createUserRole(User user, Role role) {
            return new UserRole()
                    .setUser(user)
                    .setRole(role);
        }
    }

    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        private User user;
        private Role role;

        @BeforeEach
        void setUp() {
            user = userRepository.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));
        }

        @Test
        @DisplayName("Saving a UserRole entity should persist it correctly.")
        void givenValidUserRole_whenSave_thenEntityIsPersisted() {
            var userRole = EntityFixture.createUserRole(user, role);

            var actual = systemUnderTest.save(userRole);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(user.getId(), actual.getUser().getId());
            assertEquals(role.getId(), actual.getRole().getId());
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        private Long userRoleId;

        @BeforeEach
        void setUp() {
            var user = userRepository.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            var role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));

            var userRole = systemUnderTest.save(EntityFixture.createUserRole(user, role));
            userRoleId = userRole.getId();
        }

        @Test
        @DisplayName("Retrieving a UserRole entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnUserRole() {
            var actual = systemUnderTest.findById(userRoleId);

            assertTrue(actual.isPresent());
            assertEquals(userRoleId, actual.get().getId());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        private Long userRoleId;
        private User user;
        private Role role;

        @BeforeEach
        void setUp() {
            user = userRepository.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));

            var userRole = systemUnderTest.save(EntityFixture.createUserRole(user, role));
            userRoleId = userRole.getId();
        }

        @Test
        @DisplayName("Updating a UserRole entity should persist the changes.")
        void givenUpdatedUserRole_whenUpdate_thenChangesPersisted() {
            var newRole = roleRepository.save(EntityFixture.createRole("ROLE_ADMIN"));
            var userRole = systemUnderTest.findById(userRoleId).get();
            userRole.setRole(newRole);

            var actual = systemUnderTest.save(userRole);

            assertEquals(newRole.getId(), actual.getRole().getId());
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        private Long userRoleId;

        @BeforeEach
        void setUp() {
            var user = userRepository.save(EntityFixture.createUser("testuser", "password123", "testuser@example.com", true));
            var role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));

            var userRole = systemUnderTest.save(EntityFixture.createUserRole(user, role));
            userRoleId = userRole.getId();
        }

        @Test
        @DisplayName("Deleting a UserRole entity should remove it from the repository.")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            systemUnderTest.deleteById(userRoleId);

            var actual = systemUnderTest.findById(userRoleId);

            assertFalse(actual.isPresent());
        }
    }
}
