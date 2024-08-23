package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.service.impl.UserService;
import com.tutorial.springboot.rbac.test_utils.stub.TestDatabaseAssistant;
import com.tutorial.springboot.rbac.test_utils.stub.DtoStubFactory;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    class SaveTests {

        @Test
        void givenValidDto_whenSaveOne_thenReturnId() {
            var givenDto = DtoStubFactory.createUser(1, 0, 0).asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenUserWithRoleAndPermission_whenSaveOne_thenReturnId() {
            var givenDto = DtoStubFactory.createUser(1, 1, 1).asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindById_thenReturnDto() {
            var givenId = testDatabaseAssistant.insertTestUser(1, 0, 0)
                    .dto()
                    .asOne()
                    .getId();
            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertFalse(actual.get().getUsername().isEmpty());
            assertFalse(actual.get().getEmail().isEmpty());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDto_whenUpdate_thenJustRunSuccessful() {
            var givenDto = testDatabaseAssistant.insertTestUser(1, 1, 1)
                    .dto()
                    .asOne()
                    .setUsername("newusername")
                    .setPassword("newpassword")
                    .setEmail("newusername@host.com")
                    .setEnabled(false);
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.selectTestUser().dto().asOne();

            assertNotNull(actual);
            assertEquals("newusername", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("newusername@host.com", actual.getEmail());
            assertFalse(actual.isEnabled());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestUser(1, 0, 0)
                    .dto()
                    .asOne()
                    .getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.selectTestUser().dto().asOne();

            assertNull(actual);
        }
    }

    @Nested
    class CustomMethodTests {

        @Test
        void givenUsername_whenFindByUsername_thenReturnUser() {
            var givenUser = testDatabaseAssistant.insertTestUserAndLogin();
            var givenUserUsername = givenUser.getUsername();

            var actual = systemUnderTest.findByUsername(givenUserUsername);

            assertNotNull(actual);
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
        }

        @Test
        void givenOldPasswordAndNewPassword_whenChangePassword_thenUpdatePassword() {
            var givenUser = testDatabaseAssistant.insertTestUserAndLogin();
            var givenUserPassword = givenUser.getPassword();

            systemUnderTest.changePassword(givenUserPassword, "updated_password");
            var actual = testDatabaseAssistant.selectTestUser().dto().asOne();

            assertNotNull(actual);
            assertFalse(actual.getPassword().isEmpty());
        }

    }
}