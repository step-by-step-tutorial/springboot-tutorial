package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.fixture.EntityStubFactory;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.tutorial.springboot.rbac.fixture.StubFactory.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
public class UserRepositoryTest {

    @Autowired
    UserRepository systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    class CreateTest {

        @Test
        void givenValidEntity_whenSave_thenReturnPersistedEntity() {
            var givenEntity = (User) new EntityStubFactory().addUser().get().asOne();

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals("test_0", actual.getUsername());
            assertEquals(TEST_USER_PASSWORD, actual.getPassword());
            assertEquals("test_0@example.com", actual.getEmail());
            assertTrue(actual.isEnabled());
        }

        @Test
        void givenValidEntities_whenSaveAll_thenReturnPersistedEntities() {
            var users = (List<User>) new EntityStubFactory()
                    .addUser()
                    .addUser()
                    .addUser()
                    .get()
                    .asList();

            var actual = systemUnderTest.saveAll(users);

            assertNotNull(actual);
            assertEquals(3, actual.size());
            assertTrue(actual.stream().allMatch(user -> user.getId() != null));
        }

        @Test
        void givenUserIncludeRoleAndPermission_whenSave_thenReturnEntityIncludeId() {
            var givenUser = (User) new EntityStubFactory()
                    .addPermission()
                    .addRole()
                    .addUser()
                    .get()
                    .asOne();

            var actual = systemUnderTest.save(givenUser);

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
            var givenId = testDatabaseAssistant.newTestUser().asEntity.getId();

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
            var givenEntity = testDatabaseAssistant.newTestUser().asEntity
                    .setUsername("newusername")
                    .setPassword("newpassword")
                    .setEmail("newusername@example.com")
                    .setEnabled(false);

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(givenEntity.getId(), actual.getId());
            assertEquals("newusername", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("newusername@example.com", actual.getEmail());
            assertFalse(actual.isEnabled());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenID_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.newTestUser().asEntity.getId();

            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}