package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant.PermissionTestAssistant;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.PermissionTestFactory;
import com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.removeDuplication;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository systemUnderTest;

    @Autowired
    private PermissionTestAssistant assistant;

    @Autowired
    private PermissionTestFactory factory;

    @Nested
    class CreateTest {

        @Test
        void givenEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = factory.newInstances(1).entity().asOne();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getName().isEmpty());
        }

        @Test
        void givenListOfEntities_whenSaveAll_thenReturnListOfPersistedEntity() {
            var numberOfEntities = 2;
            var givenEntities = factory.newInstances(numberOfEntities).entity().asUniqList(Permission::getName);

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
            var givenId = assistant.populate(1).entity().asOne().getId();

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
            var givenEntity = assistant.populate(1)
                    .entity()
                    .asOne()
                    .setName("updated_value");

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
            var givenId = assistant.populate(1)
                    .entity()
                    .asOne()
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}
