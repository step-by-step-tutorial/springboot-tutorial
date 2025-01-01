package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityFixture;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityAssertionUtils.assertPermissions;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.newGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityAssertionUtils.assertRoles;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityFixture.newGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityAssertionUtils.assertUser;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityAssertionUtils.assertUsers;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityFixture.newGivenUser;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityFixture.persistedGivenUser;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository systemUnderTest;

    @Autowired
    private EntityManager assistant;

    @BeforeEach
    void setUp() {
        login();
    }

    @Nested
    class SaveTest {

        @Test
        void givenNewUser_whenSave_thenPersistedUserShouldBeReturned() {
            var givenUser = newGivenUser();

            var actual = systemUnderTest.save(givenUser);
            assistant.flush();

            assertUser(actual, 1, 0);
        }

        @Test
        void givenNewUserWithRole_whenSave_thenPersistedUserWithRoleShouldBeReturned() {
            var givenUser = newGivenUser(newGivenRole());

            var actual = systemUnderTest.save(givenUser);
            assistant.flush();

            assertUser(actual, 1, 1);
            assertRoles(actual.getRoles(), 1, new long[]{1}, new int[]{0});
        }

        @Test
        void givenNewUserWithRoleAndPermission_whenSave_thenPersistedUserWithRoleAndPermissionShouldBeReturned() {
            var givenUser = newGivenUser(newGivenRole(newGivenPermission()));

            var actual = systemUnderTest.save(givenUser);
            assistant.flush();

            assertUser(actual, 1, 1);
            assertRoles(actual.getRoles(), 1, new long[]{1}, new int[]{1});
            actual.getRoles().forEach(role -> assertPermissions(role.getPermissions(), 1, new long[]{1}, new int[]{0}));
        }
    }

    @Nested
    class SaveAllTest {

        @Test
        void givenListOfUsers_whenSaveAll_thenListOfPersistedUsersShouldBeReturned() {
            var givenUsers = List.of(newGivenUser("username1"), newGivenUser("username2"));

            var actual = systemUnderTest.saveAll(givenUsers);
            assistant.flush();

            assertUsers(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }
    }

    @Nested
    class FindTest {

        @Test
        void givenUserId_whenFindById_thenMatchingUserShouldBeReturned() {
            var givenId = persistedGivenUser(assistant).getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(actualItem -> assertUser(actualItem, 1, 0));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUserDetails_whenUpdate_thenUpdatedPersistedUserShouldBeReturned() {
            var user = persistedGivenUser(assistant);

            var givenId = user.getId();
            var givenUser = newGivenUser("newusername");

            var actual = Assertions.assertDoesNotThrow(() -> {
                var updatedUser = assistant.find(User.class, givenId);
                updatedUser.updateFrom(givenUser);
                var result = systemUnderTest.save(updatedUser);
                assistant.flush();
                return result;
            });

            assertUser(actual, 1, 1);
        }

        @Test
        void givenUserWithUpdatedRoles_whenUpdate_thenUserWithUpdatedRolesShouldBeReturned() {
            var role = newGivenRole("guest");
            var user = persistedGivenUser(assistant, role);
            var givenRoleId = role.getId();
            var givenUserId = user.getId();

            role = assistant.find(Role.class, givenRoleId);
            var givenRole = newGivenRole("host");
            var givenUser = newGivenUser(role, givenRole);

            var actual = assertDoesNotThrow(() -> {
                var updatedUser = assistant.find(User.class, givenUserId);
                updatedUser.updateJoinTableRelations(givenUser);
                var result = systemUnderTest.save(updatedUser);
                assistant.flush();
                return result;
            });

            assertUser(actual, 1, 1);
            assertRoles(actual.getRoles(), 2, new long[]{1, 2}, new int[]{0, 0});
        }

        @Test
        void givenUserWithUpdatedPermissions_whenUpdate_thenUserWithUpdatedPermissionsShouldBeReturned() {
            var permission = newGivenPermission("read");
            var role = newGivenRole(permission);
            var user = persistedGivenUser(assistant, role);

            var givenPermissionId = permission.getId();
            var givenRoleId = role.getId();
            var givenUserId = user.getId();

            permission = assistant.find(Permission.class, givenPermissionId);
            var givenPermission = newGivenPermission("write");
            role = assistant.find(Role.class, givenRoleId);
            role.setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenUser = newGivenUser(role);

            var actual = assertDoesNotThrow(() -> {
                var updatedUser = assistant.find(User.class, givenUserId);
                updatedUser.updateJoinTableRelations(givenUser);
                var result = systemUnderTest.save(updatedUser);
                assistant.flush();
                return result;
            });

            assertUser(actual, 1, 0);
            assertRoles(actual.getRoles(), 1, new long[]{1}, new int[]{1});
            actual.getRoles().forEach(roleItem -> assertPermissions(roleItem.getPermissions(), 2, new long[]{1, 2}, new int[]{0, 0}));
        }

        @Test
        void givenUserWithUpdatedRolesAndPermissions_whenUpdate_thenUserWithUpdatedRolesAndPermissionsShouldBeReturned() {
            var permission = newGivenPermission("read");
            var role = newGivenRole("guest", permission);
            var user = persistedGivenUser(assistant, role);

            var givenPermissionId = permission.getId();
            var givenRoleId = role.getId();
            var givenUserId = user.getId();

            permission = assistant.find(Permission.class, givenPermissionId);
            var givenPermission = newGivenPermission("write");
            role = assistant.find(Role.class, givenRoleId);
            role.setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenRole = newGivenRole("host", permission, givenPermission);
            var givenUser = newGivenUser(role, givenRole);

            var actual = assertDoesNotThrow(() -> {
                var updatedUser = assistant.find(User.class, givenUserId);
                updatedUser.updateJoinTableRelations(givenUser);
                var result = systemUnderTest.save(updatedUser);
                assistant.flush();
                return result;
            });

            assertUser(actual, 1, 1);
            assertRoles(actual.getRoles(), 2, new long[]{1, 2}, new int[]{1, 1});
            actual.getRoles().forEach(roleItem -> assertPermissions(roleItem.getPermissions(), 2, new long[]{1, 2}, new int[]{0, 0}));
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenExistingUserId_whenDeleteById_thenUserShouldBeDeletedSuccessfully() {
            var givenUser = persistedGivenUser(assistant);
            var givenId = givenUser.getId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.findById(givenId);
            });

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class ValidationTest {

        @ParameterizedTest
        @ArgumentsSource(UserEntityFixture.InvalidUsers.class)
        void givenInvalidUser_whenSave_thenRuntimeExceptionShouldBeThrown(User givenUser) {

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenUser);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenDuplicateUsers_whenSaveAll_thenRuntimeExceptionShouldBeThrown() {
            var givenUsers = List.of(newGivenUser("the same username"), newGivenUser("the same username"));

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.saveAll(givenUsers);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }
}