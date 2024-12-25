package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Role;
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

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture.*;
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

    @Nested
    class SaveOneTest {

        @Test
        void givenNewRole_whenSave_thenPersistAndReturnRoleWithValidId() {
            var givenRole = newGivenRole();

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenNewRoleWithNewPermission_whenSave_thenPersistRoleWithPermission() {
            var givenPermission = newGivenPermission();
            var givenRole = newGivenRole();
            givenRole.getPermissions().add(givenPermission);

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
            assertNotNull(actual.getPermissions());
            assertEquals(1, actual.getPermissions().size());
            assertTrue(actual.getPermissions().getFirst().getId() > 0);
            assertEquals(0, (int) actual.getPermissions().getFirst().getVersion());
        }

        @Test
        void givenNewRoleWithExistingPermission_whenSave_thenPersistRoleWithValidPermissionReference() {
            var givenPermission = newGivenPermission();
            assistant.persist(givenPermission);
            assistant.flush();
            var givenRole = newGivenRole();
            givenRole.getPermissions().add(givenPermission);

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
            assertNotNull(actual.getPermissions());
            assertEquals(1, actual.getPermissions().size());
            assertTrue(actual.getPermissions().getFirst().getId() > 0);
            assertEquals(0, (int) actual.getPermissions().getFirst().getVersion());
        }

        /**
         * This test verifies that the combination of role ID and permission ID in the join table
         * remains unique, ensuring no duplicate associations are allowed between a role and the same permission.
         */
        @Test
        void givenRoleWithDuplicatedPermissionReference_whenSave_thenThrowUniqueConstraintViolation() {
            var givenPermission = newGivenPermission();
            assistant.persist(givenPermission);
            assistant.flush();
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
            var givenEntities = List.of(new Role().setName("role a").setCreatedBy("test").setCreatedAt(now()).setVersion(0), new Role().setName("role b").setCreatedBy("test").setCreatedAt(now()).setVersion(0));
            var actual = systemUnderTest.saveAll(givenEntities);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertFalse(actualItem.getName().isEmpty());
            });
        }
    }

    @Nested
    class FindTest {

        @Test
        void givenValidRoleId_whenFindById_thenReturnMatchingRole() {
            var role = newGivenRole();
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            var givenId = role.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getName().isEmpty());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedRoleDetails_whenUpdate_thenPersistAndReturnUpdatedRole() {
            login();
            var role = newGivenRole();
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole = newGivenRole("updated_value");
            var toUpdate = assistant.find(Role.class, givenId);
            toUpdate.updateFrom(givenRole);

            var actual = systemUnderTest.save(toUpdate);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(role.getId(), actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenRoleWithAdditionalPermissions_whenUpdate_thenPersistUpdatedPermissions() {
            login();
            var readPermission = newGivenPermission("read");
            var role = newGivenRole().setPermissions(List.of(readPermission));
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole = newGivenRole().setPermissions(List.of(readPermission, newGivenPermission("write")));
            var toUpdate = assistant.find(Role.class, givenId);
            toUpdate.updateFrom(givenRole);

            var actual = systemUnderTest.save(toUpdate);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(toUpdate.getId(), actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(2, actual.getPermissions().size());
            assertEquals("read", actual.getPermissions().getFirst().getName());
            assertEquals(0, actual.getPermissions().getFirst().getVersion());
            assertEquals("write", actual.getPermissions().getLast().getName());
            assertEquals(0, actual.getPermissions().getLast().getVersion());
        }

        @Test
        void givenRoleWithReplacedPermissions_whenUpdate_thenPersistNewPermissionsSuccessfully() {
            login();
            var readPermission = newGivenPermission("read");
            var role = newGivenRole().setPermissions(List.of(readPermission));
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole = newGivenRole().setPermissions(List.of(newGivenPermission("write")));
            var toUpdate = assistant.find(Role.class, givenId);
            toUpdate.updateFrom(givenRole);

            var actual = systemUnderTest.save(toUpdate);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(toUpdate.getId(), actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(1, actual.getPermissions().size());
            assertEquals("write", actual.getPermissions().getLast().getName());
            assertEquals(0, actual.getPermissions().getLast().getVersion());
        }

        @Test
        void givenRoleWithDeletedPermissions_whenUpdate_thenPersistRoleWithoutPermissions() {
            login();
            var readPermission = newGivenPermission("read");
            var role = newGivenRole().setPermissions(List.of(readPermission));
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole = newGivenRole();
            var toUpdate = assistant.find(Role.class, givenId);
            toUpdate.updateFrom(givenRole);

            var actual = systemUnderTest.save(toUpdate);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(toUpdate.getId(), actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(0, actual.getPermissions().size());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenValidRoleId_whenDeleteById_thenRemoveRoleSuccessfully() {
            var role = new Role().setName("role").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(role);
            assistant.flush();
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

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenExistingRole_whenFindOrSave_thenReturnPersistedRole() {
            var givenRole = newGivenRole();
            assistant.persist(givenRole);
            assistant.flush();

            var actual = systemUnderTest.findOrSave(givenRole);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }
    }

    @Nested
    class FindOrSaveAllTest {
        @Test
        void givenNonExistentRoles_whenFindOrSaveAll_thenPersistAndReturnAllRoles() {
            var givenRole = List.of(newGivenRole("read"), newGivenRole("write"));

            var actual = systemUnderTest.findOrSaveAll(givenRole);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertFalse(actualItem.getName().isEmpty());
            });
        }

        @Test
        void givenAllExistingRoles_whenFindOrSaveAll_thenReturnPersistedRoles() {
            var guestRole = newGivenRole("guest");
            assistant.persist(guestRole);
            var hostRole = newGivenRole("host");
            assistant.persist(hostRole);
            assistant.flush();

            var givenRoles = List.of(guestRole, hostRole);

            var actual = systemUnderTest.findOrSaveAll(givenRoles);

            assertNotNull(actual);
            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertFalse(actualItem.getName().isEmpty());
            });
        }

        @Test
        void givenMixedRoles_whenFindOrSaveAll_thenPersistNewRolesAndReturnAllRoles() {
            var guestRole = newGivenRole("guest");
            var hostRole = newGivenRole("host");
            assistant.persist(hostRole);
            assistant.flush();

            var givenRoles = List.of(guestRole, hostRole);

            var actual = systemUnderTest.findOrSaveAll(givenRoles);

            assertNotNull(actual);
            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertFalse(actualItem.getName().isEmpty());
            });
        }
    }

    @Nested
    class ValidationTest {

        @ParameterizedTest
        @ArgumentsSource(InvalidRoles.class)
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