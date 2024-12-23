package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
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
    class CreateTest {

        @Test
        void givenEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = new Permission()
                    .setName("permission")
                    .setCreatedBy("test").setCreatedAt(now())
                    .setVersion(0);

            var actual = systemUnderTest.save(givenEntity);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenEntityWithNullVersion_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = new Permission()
                    .setName("permission")
                    .setCreatedBy("test")
                    .setCreatedAt(now())
                    .setVersion(null);

            var actual = systemUnderTest.save(givenEntity);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertNotNull(actual.getVersion());
            assertTrue(actual.getVersion() >= 0);
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenEntityWithNegativeVersion_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = new Permission()
                    .setName("permission")
                    .setCreatedBy("test")
                    .setCreatedAt(now())
                    .setVersion(-1);

            var actual = systemUnderTest.save(givenEntity);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertNotNull(actual.getVersion());
            assertTrue(actual.getVersion() >= 0);
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenListOfUniqueEntities_whenSaveAll_thenReturnListOfPersistedEntity() {
            var givenEntities = List.of(
                    new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0),
                    new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0)
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
    class ReadTest {

        @Test
        void givenId_whenFindById_thenReturnEntity() {
            var entity = new Permission().setName("permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
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
        void givenUpdatedEntity_whenUpdate_thenReturnUpdatedEntity() {
            var givenEntity = new Permission().setName("permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(givenEntity);
            assistant.flush();
            givenEntity.setName("updated_value");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var entity = new Permission().setName("permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(entity);
            assistant.flush();
            var givenId = entity.getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    class FindOrCreateTest {
        @Test
        void givenNonExistEntity_whenFindOrCreate_thenReturnPersistedEntity() {
            var givenEntity = new Permission()
                    .setName("permission")
                    .setCreatedBy("test")
                    .setCreatedAt(now())
                    .setVersion(0);

            var actual = systemUnderTest.findOrCreate(givenEntity);
            assistant.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenExistEntity_whenFindOrCreate_thenReturnPersistedEntity() {
            var givenEntity = new Permission()
                    .setName("permission")
                    .setCreatedBy("test")
                    .setCreatedAt(now())
                    .setVersion(0);
            assistant.persist(givenEntity);
            assistant.flush();


            var actual = systemUnderTest.findOrCreate(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertFalse(actual.getName().isEmpty());
        }
    }

    @Nested
    class FindOrCreateAllTest {
        @Test
        void givenNonExistEntities_whenFindOrCreateAll_thenReturnPersistedEntity() {
            var givenEntity = List.of(
                    new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0),
                    new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0)
            );

            var actual = systemUnderTest.findOrCreateAll(givenEntity);
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
        void givenExistEntities_whenFindOrCreateAll_thenReturnPersistedEntity() {
            var readPermission = new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(readPermission);
            var writePermission = new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(writePermission);
            assistant.flush();

            var givenEntities = List.of(readPermission, writePermission);

            var actual = systemUnderTest.findOrCreateAll(givenEntities);

            assertNotNull(actual);
            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertFalse(actualItem.getName().isEmpty());
            });
        }

        @Test
        void givenExistAndNonExistEntities_whenFindOrCreateAll_thenReturnPersistedEntity() {
            var readPermission = new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            var writePermission = new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(writePermission);
            assistant.flush();

            var givenEntities = List.of(readPermission, writePermission);

            var actual = systemUnderTest.findOrCreateAll(givenEntities);

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
        @ArgumentsSource(InvalidPermissionEntity.class)
        void givenInvalidEntity_whenSaveOne_thenReturnRuntimeException(Permission givenEntity) {

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

    static class InvalidPermissionEntity implements ArgumentsProvider {
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
