package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture;
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

    @BeforeEach
    void setUp() {
        login();
    }

    @Nested
    class SaveOneTest {

        @Test
        void givenNewPermission_whenSave_thenShouldReturnPersistedPermission() {
            var givenPermission = newGivenPermission();

            var actual = systemUnderTest.save(givenPermission);
            assistant.flush();

            assertPermission(actual, 1, 0);
        }

        @Test
        void givenNewPermissionWithNullVersion_whenSave_thenShouldReturnPersistedPermission() {
            var givenPermission = newGivenPermission().setVersion(null);

            var actual = systemUnderTest.save(givenPermission);
            assistant.flush();

            assertPermission(actual, 1, 0);
        }

        @Test
        void givenNewPermissionWithNegativeVersion_whenSave_thenShouldReturnPersistedPermissionWithPositiveVersion() {
            var givenPermission = newGivenPermission().setVersion(-1);

            var actual = systemUnderTest.save(givenPermission);
            assistant.flush();

            assertPermission(actual, 1, 0);
        }
    }

    @Nested
    class SaveAllTest {
        @Test
        void givenListOfUniquePermissions_whenSaveAll_thenShouldReturnPersistedPermissions() {
            var givenPermissions = List.of(newGivenPermission("read"), newGivenPermission("write"));

            var actual = systemUnderTest.saveAll(givenPermissions);
            assistant.flush();

            assertPermissions(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }
    }

    @Nested
    class FindTest {

        @Test
        void givenPermissionId_whenFindById_thenShouldReturnPermission() {
            var permission = persistedGivenPermission(assistant);
            var givenId = permission.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(actualItem -> assertPermission(actualItem, 1, 0));
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenExistingPermission_whenUpdate_thenShouldReturnUpdatedPermission() {
            var permission = persistedGivenPermission(assistant);

            var givenId = permission.getId();
            var givenPermission = newGivenPermission("updated_value");

            var actual = assertDoesNotThrow(() -> {
                var updatedPermission = assistant.find(Permission.class, givenId);
                updatedPermission.updateFrom(givenPermission);
                var result = systemUnderTest.save(updatedPermission);
                assistant.flush();
                return result;
            });

            assertPermission(actual, 1, 1);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenPermissionId_whenDeleteById_thenShouldNotFindPermission() {
            var permission = persistedGivenPermission(assistant);

            var givenId = permission.getId();

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
        void givenNonExistentPermission_whenFindOrSave_thenShouldReturnPersistedPermission() {
            var givenPermission = newGivenPermission();

            var actual = systemUnderTest.findOrSave(givenPermission);
            assistant.flush();

            assertPermission(actual, 1, 0);
        }

        @Test
        void givenExistingPermission_whenFindOrSave_thenShouldReturnPersistedPermission() {
            var givenPermission = persistedGivenPermission(assistant);

            var actual = systemUnderTest.findOrSave(givenPermission);
            assistant.flush();

            assertPermission(actual, 1, 0);
        }
    }

    @Nested
    class FindOrSaveAllTest {
        @Test
        void givenNonExistentPermissions_whenFindOrSaveAll_thenShouldReturnPersistedPermissions() {
            var givenPermissions = List.of(newGivenPermission("read"), newGivenPermission("write"));

            var actual = systemUnderTest.findOrSaveAll(givenPermissions);
            assistant.flush();

            assertPermissions(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }

        @Test
        void givenExistingPermissions_whenFindOrSaveAll_thenShouldReturnPersistedPermissions() {
            var givenPermissions = List.of(persistedGivenPermission(assistant, "read"), persistedGivenPermission(assistant, "write"));

            var actual = systemUnderTest.findOrSaveAll(givenPermissions);
            assistant.flush();

            assertPermissions(actual, 2, new long[]{1, 2}, new int[]{0, 0});
        }

        @Test
        void givenMixOfExistingAndNonExistentPermissions_whenFindOrSaveAll_thenShouldReturnPersistedPermissions() {
            var givenPermissions = List.of(persistedGivenPermission(assistant, "write"), newGivenPermission("read"));

            var actual = systemUnderTest.findOrSaveAll(givenPermissions);

            assertPermissions(actual, 2, new long[]{2, 1}, new int[]{0, 0});
        }
    }

    @Nested
    class ValidationTest {

        @ParameterizedTest
        @ArgumentsSource(PermissionEntityFixture.InvalidPermissions.class)
        void givenInvalidPermission_whenSave_thenShouldThrowRuntimeException(Permission givenPermission) {

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.save(givenPermission);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }

        @Test
        void givenListOfNonUniquePermissions_whenSaveAll_thenShouldThrowRuntimeException() {
            var givenEntities = List.of(
                    new Permission().setName("the same permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0),
                    new Permission().setName("the same permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0));

            var actual = assertThrows(RuntimeException.class, () -> {
                systemUnderTest.saveAll(givenEntities);
                assistant.flush();
            });

            assertNotNull(actual);
            assertNotNull(actual.getMessage());
        }
    }

}
