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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
    class CreateTest {

        @Test
        void givenEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = new Role()
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

        @Test
        void givenRoleWithNewPermission_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = new Role()
                    .setName("role with new permission")
                    .setCreatedBy("test").setCreatedAt(now())
                    .setVersion(0)
                    .setPermissions(List.of(new Permission().setName("permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0)));

            var actual = systemUnderTest.save(givenEntity);
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
        void givenRoleWithExistsPermission_whenSaveOne_thenReturnPersistedEntity() {
            var permission = new Permission().setName("permission").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(permission);

            var givenEntity = new Role()
                    .setName("role with exists permission")
                    .setCreatedBy("test").setCreatedAt(now())
                    .setVersion(0)
                    .setPermissions(List.of(permission));

            var actual = systemUnderTest.save(givenEntity);
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
    }

    @Nested
    class ReadTest {

        @Test
        void givenId_whenFindById_thenReturnEntity() {
            var role = new Role().setName("role").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(role);
            assistant.flush();
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
            var givenEntity = new Role().setName("role").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            assistant.persist(givenEntity);
            assistant.flush();
            givenEntity.setName("updated_value");

            var actual = systemUnderTest.save(givenEntity);
            assistant.flush();

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenRoleWithUpdatedPermissionList_whenUpdate_thenReturnUpdatedEntity() {
            var permission = new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            var givenRole = new Role()
                    .setName("role")
                    .setCreatedBy("test").setCreatedAt(now())
                    .setVersion(0)
                    .setPermissions(new ArrayList<>(List.of(permission)));
            assistant.persist(givenRole);
            assistant.flush();

            givenRole.setPermissions(new ArrayList<>(List.of(permission, new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0))));

            var actual = systemUnderTest.save(givenRole);

            assertNotNull(actual);
            assertEquals(givenRole.getId(), actual.getId());
            assertNotNull(actual.getPermissions());
            assertEquals(2, actual.getPermissions().size());
        }

        @Test
        void givenRoleWithReplacedPermission_whenUpdate_thenReturnUpdatedEntity() {
            var permission = new Permission().setName("read").setCreatedBy("test").setCreatedAt(now()).setVersion(0);
            var givenRole = new Role()
                    .setName("role")
                    .setCreatedBy("test").setCreatedAt(now())
                    .setVersion(0)
                    .setPermissions(new ArrayList<>(List.of(permission)));
            assistant.persist(givenRole);
            assistant.flush();

            givenRole.setPermissions(new ArrayList<>(List.of(new Permission().setName("write").setCreatedBy("test").setCreatedAt(now()).setVersion(0))));

            var actual = systemUnderTest.save(givenRole);

            assertNotNull(actual);
            assertEquals(givenRole.getId(), actual.getId());
            assertNotNull(actual.getPermissions());
            assertEquals(1, actual.getPermissions().size());
            assertEquals("write", actual.getPermissions().getFirst().getName());
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
        @ArgumentsSource(InvalidRoleEntity.class)
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

    static class InvalidRoleEntity implements ArgumentsProvider {
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