package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityFixture;
import jakarta.persistence.EntityManager;
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

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityAssertionUtils.assertPermission;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityAssertionUtils.assertPermissions;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.newGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.persistedGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityAssertionUtils.assertRole;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityAssertionUtils.assertRoles;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityFixture.newGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityFixture.persistedGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository systemUnderTest;

    @Autowired
    private EntityManager assistant;

    @BeforeEach
    void setUp() {
        login();
    }

    @Nested
    class SaveOneTest {

        @Test
        void givenNewRole_whenSave_thenPersistAndReturnRoleWithValidId() {
            var givenRole = newGivenRole();

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertRole(actual, 1, 0);
        }

        @Test
        void givenNewRoleWithNewPermission_whenSave_thenPersistRoleWithPermission() {
            var givenPermission = newGivenPermission();
            var givenRole = newGivenRole();
            givenRole.getPermissions().add(givenPermission);

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertRole(actual, 1, 1);
            assertPermission(actual.getPermissions().getFirst(), 1, 0);
        }

        @Test
        void givenNewRoleWithExistingPermission_whenSave_thenPersistRoleWithValidPermissionReference() {
            var givenPermission = persistedGivenPermission(assistant);
            var givenRole = newGivenRole();
            givenRole.getPermissions().add(givenPermission);

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertRole(actual, 1, 1);
            assertPermissions(actual.getPermissions(), 1, new long[]{1}, new int[]{1});
        }

        /**
         * This test verifies that the combination of role ID and permission ID in the join table
         * remains unique, ensuring no duplicate associations are allowed between a role and the same permission.
         */
        @Test
        void givenRoleWithDuplicatedPermissionReference_whenSave_thenThrowUniqueConstraintViolation() {
            var givenPermission = persistedGivenPermission(assistant);
            var givenRole = newGivenRole();
            givenRole.getPermissions().add(givenPermission);
            givenRole.getPermissions().add(givenPermission);

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenRole);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenRoleWithUniquePermissions_whenSave_thenThrowRuntimeExceptionDueToDuplicateValues() {
            var givenPermission1 = newGivenPermission();
            var givenPermission2 = newGivenPermission();

            var givenRole = newGivenRole();
            givenRole.getPermissions().add(givenPermission1);
            givenRole.getPermissions().add(givenPermission2);

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenRole);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

    @Nested
    class SaveAllTest {
        @Test
        void givenUniqueRolesList_whenSaveAll_thenPersistAllRolesSuccessfully() {
            var givenEntities = List.of(newGivenRole("role a"), newGivenRole("role b"));

            var actual = systemUnderTest.saveAll(givenEntities);
            assistant.flush();

            assertRoles(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }
    }

    @Nested
    class FindTest {

        @Test
        void givenValidRoleId_whenFindById_thenReturnMatchingRole() {
            var role = persistedGivenRole(assistant);
            var givenId = role.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(actualItem -> assertRole(actualItem, 1, 0));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedRole_whenUpdate_thenPersistAndReturnUpdatedRole() {
            var role = persistedGivenRole(assistant);

            var givenId = role.getId();
            var givenRole = newGivenRole("updated_value");

            var actual = assertDoesNotThrow(() -> {
                var entity = assistant.find(Role.class, givenId);
                entity.updateFrom(givenRole);
                var result = systemUnderTest.save(entity);
                assistant.flush();
                return result;
            });

            assertRole(actual, 1, 1);
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenRoleWithAdditionalPermissions_whenUpdate_thenPersistUpdatedPermissions() {
            var permission = newGivenPermission("read");
            var role = persistedGivenRole(assistant, permission);

            var givenId = role.getId();
            var givenRole = newGivenRole(permission, newGivenPermission("write"));

            var actual = assertDoesNotThrow(() -> {
                var entity = assistant.find(Role.class, givenId);
                entity.updateFrom(givenRole);
                var result = systemUnderTest.save(entity);
                assistant.flush();
                return result;
            });

            assertRole(actual, 1, 1);
            assertPermissions(actual.getPermissions(), 2, new long[]{1, 2}, new int[]{0, 0});
        }

        @Test
        void givenRoleWithReplacedPermissions_whenUpdate_thenPersistNewPermissionsSuccessfully() {
            var role = persistedGivenRole(assistant, newGivenPermission("read"));

            var givenId = role.getId();
            var givenRole = newGivenRole(newGivenPermission("write"));

            var actual = assertDoesNotThrow(() -> {
                var entity = assistant.find(Role.class, givenId);
                entity.updateFrom(givenRole);
                var result = systemUnderTest.save(entity);
                assistant.flush();
                return result;
            });

            assertRole(actual, 1, 1);
            assertPermissions(actual.getPermissions(), 1, new long[]{2}, new int[]{0});
        }

        @Test
        void givenRoleWithDeletedPermissions_whenUpdate_thenPersistRoleWithoutPermissions() {
            var role = persistedGivenRole(assistant, newGivenPermission("read"));

            var givenId = role.getId();
            var givenRole = newGivenRole();

            var actual = assertDoesNotThrow(() -> {
                var entity = assistant.find(Role.class, givenId);
                entity.updateFrom(givenRole);
                var result = systemUnderTest.save(entity);
                assistant.flush();
                return result;
            });

            assertRole(actual, 1, 1);
            assertNotNull(actual.getPermissions());
            assertEquals(0, actual.getPermissions().size());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenValidRoleId_whenDeleteById_thenRemoveRoleSuccessfully() {
            var role = persistedGivenRole(assistant);

            var givenId = role.getId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.findById(givenId);
            });

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class FindOrSaveTest {
        @Test
        void givenNonExistentRole_whenFindOrSave_thenPersistAndReturnRole() {
            var givenRole = newGivenRole();

            var actual = systemUnderTest.findOrSave(givenRole);
            assistant.flush();

            assertRole(actual, 1, 0);
        }

        @Test
        void givenExistingRole_whenFindOrSave_thenReturnPersistedRole() {
            var givenRole = persistedGivenRole(assistant);

            var actual = systemUnderTest.findOrSave(givenRole);
            assistant.flush();

            assertRole(actual, 1, 0);
        }
    }

    @Nested
    class FindOrSaveAllTest {
        @Test
        void givenNonExistentRoles_whenFindOrSaveAll_thenPersistAndReturnAllRoles() {
            var givenRole = List.of(newGivenRole("read"), newGivenRole("write"));

            var actual = systemUnderTest.findOrSaveAll(givenRole);
            assistant.flush();

            assertRoles(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }

        @Test
        void givenAllExistingRoles_whenFindOrSaveAll_thenReturnPersistedRoles() {
            var guestRole = persistedGivenRole(assistant, "guest");
            var hostRole = persistedGivenRole(assistant, "host");

            var givenRoles = List.of(guestRole, hostRole);

            var actual = systemUnderTest.findOrSaveAll(givenRoles);

            assertRoles(actual, 2, new long[]{2, 1}, new int[]{0, 0});
        }

        @Test
        void givenMixedRoles_whenFindOrSaveAll_thenPersistNewRolesAndReturnAllRoles() {
            var guestRole = newGivenRole("guest");
            var hostRole = persistedGivenRole(assistant, "host");

            var givenRoles = List.of(guestRole, hostRole);

            var actual = systemUnderTest.findOrSaveAll(givenRoles);

            assertRoles(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }
    }

    @Nested
    class ValidationTest {

        @ParameterizedTest
        @ArgumentsSource(RoleEntityFixture.InvalidRoles.class)
        void givenInvalidRoleData_whenSaveOne_thenThrowRuntimeException(Role givenRole) {

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenRole);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenRolesWithNonUniqueNames_whenSaveAll_thenThrowRuntimeException() {
            var givenEntities = List.of(new Role().setName("the same role").setCreatedBy("test").setCreatedAt(now()).setVersion(0), new Role().setName("the same role").setCreatedBy("test").setCreatedAt(now()).setVersion(0));

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.saveAll(givenEntities);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

}