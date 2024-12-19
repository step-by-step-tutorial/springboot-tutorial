package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant.UserTestAssistant;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.RoleTestFactory;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.UserTestFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository systemUnderTest;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserTestAssistant assistant;

    @Autowired
    private UserTestFactory factory;

    @Nested
    class CreateTest {

        @Test
        void givenEntity_whenSaveOne_thenReturnPersistedEntity() {
            var givenEntity = factory.makeUniqueRelations().newInstances(1).entity().asOne();
            givenEntity.setRoles(roleRepository.findOrBatchSave(givenEntity.getRoles()));

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getPassword().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
            assertTrue(actual.isEnabled());
        }

        @Test
        void givenListOfEntity_whenSaveAll_thenReturnListOfPersistedEntity() {
            int numberOfEntities = 2;
            var givenEntities = factory.makeUniqueRelations().newInstances(numberOfEntities).entity().asUniqList(User::getUsername);
            givenEntities.forEach(user -> user.setRoles(roleRepository.findOrBatchSave(user.getRoles())));

            var actual = systemUnderTest.saveAll(givenEntities);

            assertNotNull(actual);
            assertTrue(actual.size() > 0);
            assertTrue(actual.stream().allMatch(user -> user.getId() != null));
        }

        @Test
        void givenUserWithRoleAndPermission_whenSave_thenReturnPersistedEntity() {
            var givenEntity = factory.makeUniqueRelations().newInstances(1).entity().asOne();
            givenEntity.setRoles(roleRepository.findOrBatchSave(givenEntity.getRoles()));

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getPassword().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
            assertTrue(actual.isEnabled());
            assertThat(actual.getAuthorities().stream().map(GrantedAuthority::getAuthority)).isNotEmpty();
            assertThat(actual.getPermissions()).isNotEmpty();
        }
    }

    @Nested
    class ReadTest {

        @Test
        void givenID_whenFindById_thenReturnEntity() {
            var givenEntity = assistant.makeUniqueRelations().populate(1).entity().asOne();
            givenEntity.setRoles(roleRepository.findOrBatchSave(givenEntity.getRoles()));
            var givenId = givenEntity.getId();


            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getUsername().isEmpty());
            assertFalse(actual.get().getPassword().isEmpty());
            assertFalse(actual.get().getEmail().isEmpty());
            assertTrue(actual.get().isEnabled());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedEntity_whenUpdate_thenJustRunSuccessful() {
            var givenEntity = assistant.makeUniqueRelations().populate(1)
                    .entity()
                    .asOne()
                    .setUsername("newusername")
                    .setPassword("newpassword")
                    .setEmail("newusername@host.com")
                    .setEnabled(false);
            givenEntity.setRoles(roleRepository.findOrBatchSave(givenEntity.getRoles()));

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("newusername", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("newusername@host.com", actual.getEmail());
            assertFalse(actual.isEnabled());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenEntity = assistant.makeUniqueRelations().populate(1).entity().asOne();
            givenEntity.setRoles(roleRepository.findOrBatchSave(givenEntity.getRoles()));
            var givenId = givenEntity
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}