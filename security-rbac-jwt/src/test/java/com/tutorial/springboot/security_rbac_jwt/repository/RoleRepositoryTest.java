package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils.login;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestUtils.generateString;
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
        void givenNewRole_whenSave_thenReturnPersistedRole() {
            var givenRole = newGivenRole();

            var actual = systemUnderTest.save(givenRole);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenNewRoleWithNewPermission_whenSave_thenReturnPersistedRole() {
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
        void givenNewRoleWithExistingPermission_whenSave_thenReturnPersistedRole() {
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
        void givenNewRoleWithDuplicatedSamePermissionReference_whenSave_thenThrowRuntimeException() {
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
        void givenRoleWithDuplicatedPermission_whenSave_thenReturnPersistedRole() {
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
        void givenUniqueListOfEntity_whenSaveAll_thenReturnListOfPersistedEntity() {
            var givenEntities = List.of(
                    new Role().setName("role a").setCreatedBy("test").setCreatedAt(now()).setVersion(0),
                    new Role().setName("role b").setCreatedBy("test").setCreatedAt(now()).setVersion(0)
            );
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
        void givenId_whenFindById_thenReturnEntity() {
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
        void givenUpdatedEntity_whenUpdate_thenReturnUpdatedEntity() {
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
        void givenRoleWithUpdatedPermissionList_whenUpdate_thenReturnUpdatedEntity() {
            login();
            var readPermission = newGivenPermission("read");
            var role = newGivenRole().setPermissions(List.of(readPermission));
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole  = newGivenRole().setPermissions(List.of(readPermission, newGivenPermission("write")));
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
        void givenRoleWithReplacedPermission_whenUpdate_thenReturnUpdatedEntity() {
            login();
            var readPermission = newGivenPermission("read");
            var role = newGivenRole().setPermissions(List.of(readPermission));
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole  = newGivenRole().setPermissions(List.of(newGivenPermission("write"))) ;
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
        void givenRoleWithDeletedPermission_whenUpdate_thenReturnUpdatedEntity() {
            login();
            var readPermission = newGivenPermission("read");
            var role = newGivenRole().setPermissions(List.of(readPermission));
            assistant.persist(role);
            assistant.flush();
            assistant.clear();
            assistant.detach(role);
            var givenId = role.getId();
            var givenVersion = role.getVersion();

            var givenRole  = newGivenRole() ;
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
        void givenId_whenDeleteById_thenJustRunSuccessful() {
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
    class ValidationTest {

        @ParameterizedTest
        @ArgumentsSource(InvalidRoles.class)
        void givenInvalidEntity_whenSaveOne_thenReturnRuntimeException(Role givenEntity) {

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenEntity);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenListOfNonUniqueEntities_whenSaveAll_thenReturnRuntimeException() {
            var givenEntities = List.of(
                    new Role().setName("the same role").setCreatedBy("test").setCreatedAt(now()).setVersion(0),
                    new Role().setName("the same role").setCreatedBy("test").setCreatedAt(now()).setVersion(0)
            );

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.saveAll(givenEntities);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

    private Role newGivenRole() {
        return new Role()
                .setName("role")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Role newGivenRole(String name) {
        return new Role()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Permission newGivenPermission() {
        return new Permission()
                .setName("permission")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Permission newGivenPermission(String name) {
        return new Permission()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    static class InvalidRoles implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Role()),
                    Arguments.of(new Role().setName(null).setCreatedBy("user").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Role().setName("").setCreatedBy("user").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Role().setName(generateString(51)).setCreatedBy("user").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Role().setName("role").setCreatedBy(null).setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Role().setName("role").setCreatedBy("").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Role().setName("role").setCreatedBy("user").setCreatedAt(null).setVersion(0))
            );
        }
    }
}