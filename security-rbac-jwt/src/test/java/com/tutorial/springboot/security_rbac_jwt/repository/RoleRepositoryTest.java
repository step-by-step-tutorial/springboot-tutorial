package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant.RoleTestAssistant;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.RoleTestFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository systemUnderTest;

    @Autowired
    private PermissionRepository permissionRepository;

    @Autowired
    private RoleTestAssistant assistant;

    @Autowired
    private RoleTestFactory factory;

    @Nested
    class CreateTest {

        @Test
        void givenEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = factory.makeUniqueRelations().newInstances(1).entity().asOne();
            givenEntity.setPermissions(permissionRepository.findOrCreateAll(givenEntity.getPermissions()));

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenListOfEntity_whenSaveAll_thenReturnListOfPersistedEntity() {
            var numberOfEntities = 2;
            var givenEntities = factory.makeUniqueRelations().newInstances(numberOfEntities).entity().asUniqList(Role::getName);
            givenEntities.forEach(role -> role.setPermissions(permissionRepository.findOrCreateAll(role.getPermissions())));

            var actual = systemUnderTest.saveAll(givenEntities);

            assertNotNull(actual);
            assertTrue(actual.size() > 0);
            assertTrue(actual.stream().allMatch(entity -> entity.getId() != null));
        }
    }

    @Nested
    class ReadTest {

        @Test
        void givenId_whenFindById_thenReturnEntity() {
            var givenEntity = assistant.makeUniqueRelations().populate(1).entity().asOne();
            givenEntity.setPermissions(permissionRepository.findOrCreateAll(givenEntity.getPermissions()));
            var givenId = givenEntity
                    .getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getName().isEmpty());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedEntity_whenUpdate_thenJustRunSuccessful() {
            var givenEntity = assistant.makeUniqueRelations().populate(1)
                    .entity().asOne()
                    .setName("updated_value");
            givenEntity.setPermissions(permissionRepository.findOrCreateAll(givenEntity.getPermissions()));

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
            var givenEntity = assistant.makeUniqueRelations().populate(1).entity().asOne();
            givenEntity.setPermissions(permissionRepository.findOrCreateAll(givenEntity.getPermissions()));
            var givenId = givenEntity
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}