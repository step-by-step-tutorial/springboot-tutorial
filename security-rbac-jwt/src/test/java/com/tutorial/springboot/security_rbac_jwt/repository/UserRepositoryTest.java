package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import jakarta.persistence.EntityManager;
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

import static com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture.*;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
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

    @Nested
    class SaveTest {

        @Test
        void givenNewUser_whenSave_thenPersistedUserShouldBeReturned() {
            var givenUser = newGivenUser();

            var actual = systemUnderTest.save(givenUser);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertEquals(0, (int) actual.getVersion());
        }

        @Test
        void givenNewUserWithRole_whenSave_thenPersistedUserWithRoleShouldBeReturned() {
            var givenRole = newGivenRole();
            var givenUser = newGivenUser().setRoles(List.of(givenRole));

            var actual = systemUnderTest.save(givenUser);
            assistant.flush();

            // assert user
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(1, (int) actual.getVersion());

            // assert role
            assertNotNull(actual.getRoles());
            assertEquals(1, actual.getRoles().size());
            assertTrue(actual.getRoles().getFirst().getId() > 0);
            assertEquals(0, (int) actual.getRoles().getFirst().getVersion());
        }

        @Test
        void givenNewUserWithRoleAndPermission_whenSave_thenPersistedUserWithRoleAndPermissionShouldBeReturned() {
            var permission = newGivenPermission();
            var givenRole = newGivenRole().setPermissions(List.of(permission));
            var givenUser = newGivenUser().setRoles(List.of(givenRole));

            var actual = systemUnderTest.save(givenUser);
            assistant.flush();

            // assert user
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(1, (int) actual.getVersion());

            // assert role
            assertNotNull(actual.getRoles());
            assertEquals(1, actual.getRoles().size());
            assertTrue(actual.getRoles().getFirst().getId() > 0);
            assertEquals(1, (int) actual.getRoles().getFirst().getVersion());

            // assert permission
            assertNotNull(actual.getRoles().getFirst().getPermissions());
            assertEquals(1, actual.getRoles().getFirst().getPermissions().size());
            assertTrue(actual.getRoles().getFirst().getPermissions().getFirst().getId() > 0);
            assertEquals(0, (int) actual.getRoles().getFirst().getPermissions().getFirst().getVersion());
        }
    }

    @Nested
    class SaveAllTest {

        @Test
        void givenListOfUsers_whenSaveAll_thenListOfPersistedUsersShouldBeReturned() {
            var givenUsers = List.of(newGivenUser("username1"), newGivenUser("username2"));

            var actual = systemUnderTest.saveAll(givenUsers);
            assistant.flush();

            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertEquals(0, (int) actualItem.getVersion());
            });
        }
    }

    @Nested
    class FindTest {

        @Test
        void givenUserId_whenFindById_thenMatchingUserShouldBeReturned() {
            var givenUser = newGivenUser();
            assistant.persist(givenUser);
            assistant.flush();
            assistant.clear();
            var givenId = givenUser.getId();

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
        void givenUserDetails_whenUpdate_thenUpdatedPersistedUserShouldBeReturned() {
            login();
            var user = newGivenUser();
            assistant.persist(user);
            assistant.flush();
            assistant.clear();
            assistant.detach(user);
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            var givenUser = newGivenUser("newusername");
            var toUpdate = assistant.find(User.class, givenId);
            toUpdate.updateFrom(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());
            assertEquals("newusername", actual.getUsername());
            assertNotNull(actual.getUpdatedBy());
            assertNotNull(actual.getUpdatedAt());
        }

        @Test
        void givenUserWithUpdatedRoles_whenUpdate_thenUserWithUpdatedRolesShouldBeReturned() {
            login();
            var role = newGivenRole("guest");
            var user = newGivenUser().setRoles(List.of(role));
            assistant.persist(user);
            assistant.flush();
            assistant.clear();
            assistant.detach(user);
            var roleId = role.getId();
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            role = assistant.find(Role.class, roleId);
            var givenRole = newGivenRole("host");
            var givenUser = newGivenUser().setRoles(List.of(role, givenRole));
            var toUpdate = assistant.find(User.class, givenId);
            toUpdate.updateRelations(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion, actual.getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(0, actual.getPermissions().size());
            assertEquals(2, actual.getRoles().size());
            assertEquals("guest", actual.getRoles().getFirst().getName());
            assertEquals(0, actual.getRoles().getFirst().getVersion());
            assertEquals("host", actual.getRoles().getLast().getName());
            assertEquals(0, actual.getRoles().getLast().getVersion());
        }

        @Test
        void givenUserWithUpdatedPermissions_whenUpdate_thenUserWithUpdatedPermissionsShouldBeReturned() {
            login();
            var permission = newGivenPermission("read");
            var role = newGivenRole("guest").setPermissions(List.of(permission));
            var user = newGivenUser().setRoles(List.of(role));
            assistant.persist(user);
            assistant.flush();
            assistant.clear();
            var permissionId = permission.getId();
            var roleId = role.getId();
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            permission = assistant.find(Permission.class, permissionId);
            var givenPermission = newGivenPermission("write");
            role = assistant.find(Role.class, roleId);
            role.setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenUser = newGivenUser().setRoles(List.of(role));
            var toUpdate = assistant.find(User.class, givenId);
            toUpdate.updateRelations(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion, actual.getVersion());

            assertEquals(1, actual.getRoles().size());
            assertEquals("guest", actual.getRoles().getFirst().getName());
            assertEquals(0, actual.getRoles().getFirst().getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(2, actual.getPermissions().size());
        }

        @Test
        void givenUserWithUpdatedRolesAndPermissions_whenUpdate_thenUserWithUpdatedRolesAndPermissionsShouldBeReturned() {
            login();
            var permission = newGivenPermission("read");
            var role = newGivenRole("guest").setPermissions(List.of(permission));
            var user = newGivenUser().setRoles(List.of(role));
            assistant.persist(user);
            assistant.flush();
            assistant.clear();
            var permissionId = permission.getId();
            var roleId = role.getId();
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            permission = assistant.find(Permission.class, permissionId);
            var givenPermission = newGivenPermission("write");
            role = assistant.find(Role.class, roleId);
            role.setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenRole = newGivenRole("host").setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenUser = newGivenUser().setRoles(new ArrayList<>(List.of(role, givenRole)));
            var toUpdate = assistant.find(User.class, givenId);
            toUpdate.updateRelations(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion, actual.getVersion());

            assertEquals(2, actual.getRoles().size());
            assertEquals("guest", actual.getRoles().getFirst().getName());
            assertEquals(0, actual.getRoles().getFirst().getVersion());
            assertEquals("host", actual.getRoles().getLast().getName());
            assertEquals(0, actual.getRoles().getLast().getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(2, actual.getPermissions().size());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenExistingUserId_whenDeleteById_thenUserShouldBeDeletedSuccessfully() {
            var givenUser = newGivenUser();
            assistant.persist(givenUser);
            assistant.flush();
            assistant.clear();
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
        @ArgumentsSource(InvalidUsers.class)
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