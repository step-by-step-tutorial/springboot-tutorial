package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils;
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
import java.util.Objects;
import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils.login;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestUtils.generateString;
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
            var givenPermission =newGivenPermission().setVersion(-1);

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
            var givenPermission = List.of(newGivenPermission("read"), newGivenPermission("write"));

            var actual = systemUnderTest.findOrSaveAll(givenPermission);
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
            var readPermission = new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(readPermission);
            var writePermission = new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(writePermission);
            assistant.flush();

            var givenEntities = List.of(readPermission, writePermission);

            var actual = systemUnderTest.findOrSaveAll(givenEntities);

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

    private Permission newGivenPermission() {
        return new Permission()
                .setName("permission")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Permission newGivenPermission(String name) {
        Objects.requireNonNull(name);
        return new Permission()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    static class InvalidPermissions implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Permission()),
                    Arguments.of(new Permission().setName(null).setCreatedBy("user").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Permission().setName("").setCreatedBy("user").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Permission().setName(generateString(51)).setCreatedBy("user").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Permission().setName("permission").setCreatedBy(null).setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Permission().setName("permission").setCreatedBy("").setCreatedAt(now()).setVersion(0)),
                    Arguments.of(new Permission().setName("permission").setCreatedBy("user").setCreatedAt(null).setVersion(0))
            );
        }
    }
}
