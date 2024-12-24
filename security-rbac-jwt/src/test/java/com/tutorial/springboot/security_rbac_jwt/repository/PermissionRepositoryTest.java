package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
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

import static com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture.InvalidPermissions;
import static com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture.newGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository systemUnderTest;

    @Autowired
    private EntityManager assistant;

    @Nested
    class SaveOneTest {

        @Test
        void givenNewPermission_whenSave_thenReturnPersistedPermission() {
            var givenPermission = newGivenPermission();

            var actual = systemUnderTest.save(givenPermission);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenNewPermissionWithNullVersion_whenSave_thenReturnPersistedPermission() {
            var givenPermission = newGivenPermission().setVersion(null);

            var actual = systemUnderTest.save(givenPermission);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertNotNull(actual.getVersion());
            assertTrue(actual.getVersion() >= 0);
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenNewPermissionWithNegativeVersion_whenSave_thenReturnPersistedPermission() {
            var givenPermission = newGivenPermission().setVersion(-1);

            var actual = systemUnderTest.save(givenPermission);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertNotNull(actual.getVersion());
            assertTrue(actual.getVersion() >= 0);
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }
    }

    @Nested
    class SaveAllTest {
        @Test
        void givenListOfUniquePermissions_whenSave_thenReturnListOfPersistedPermission() {
            var givenPermissions = List.of(newGivenPermission("read"), newGivenPermission("write"));

            var actual = systemUnderTest.saveAll(givenPermissions);
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
        void givenId_whenFindById_thenReturnPermission() {
            var entity = newGivenPermission();
            assistant.persist(entity);
            assistant.flush();
            var givenId = entity.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getName().isEmpty());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedPermission_whenUpdate_thenReturnUpdatedPermission() {
            login();
            var permission = newGivenPermission();
            assistant.persist(permission);
            assistant.flush();
            assistant.clear();
            assistant.detach(permission);
            var givenId = permission.getId();
            var givenVersion = permission.getVersion();

            var givenPermission = newGivenPermission("updated_value");
            var toUpdate = assistant.find(Permission.class, givenId);
            toUpdate.updateFrom(givenPermission);

            var actual = systemUnderTest.save(toUpdate);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(givenId, actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var entity = newGivenPermission();
            assistant.persist(entity);
            assistant.flush();
            var givenId = entity.getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class FindOrSaveTest {
        @Test
        void givenNonExistPermission_whenFindOrCreate_thenReturnPersistedPermission() {
            var givenPermission = newGivenPermission();

            var actual = systemUnderTest.findOrSave(givenPermission);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenExistingPermission_whenFindOrCreate_thenReturnPersistedPermission() {
            var givenPermission = newGivenPermission();
            assistant.persist(givenPermission);
            assistant.flush();

            var actual = systemUnderTest.findOrSave(givenPermission);
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
        void givenNonExistPermissions_whenFindOrCreateAll_thenReturnPersistedPermission() {
            var givenPermissions = List.of(newGivenPermission("read"), newGivenPermission("write"));

            var actual = systemUnderTest.findOrSaveAll(givenPermissions);
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
        void givenExistingPermissions_whenFindOrCreateAll_thenReturnPersistedPermission() {
            var readPermission = newGivenPermission("read");
            assistant.persist(readPermission);
            var writePermission = newGivenPermission("write");
            assistant.persist(writePermission);
            assistant.flush();

            var givenPermissions = List.of(readPermission, writePermission);

            var actual = systemUnderTest.findOrSaveAll(givenPermissions);

            assertNotNull(actual);
            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertFalse(actualItem.getName().isEmpty());
            });
        }

        @Test
        void givenExistAndNonExistPermissions_whenFindOrCreateAll_thenReturnPersistedPermission() {
            var readPermission = newGivenPermission("read");
            var writePermission = newGivenPermission("write");
            assistant.persist(writePermission);
            assistant.flush();

            var givenPermissions = List.of(readPermission, writePermission);

            var actual = systemUnderTest.findOrSaveAll(givenPermissions);

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
        @ArgumentsSource(InvalidPermissions.class)
        void givenInvalidPermission_whenSaveOne_thenReturnRuntimeException(Permission givenPermission) {

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenPermission);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenListOfNonUniquePermissions_whenSaveAll_thenReturnRuntimeException() {
            var givenEntities = List.of(
                    new Permission().setName("the same permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0),
                    new Permission().setName("the same permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0)
            );

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.saveAll(givenEntities);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

}
