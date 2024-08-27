package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.service.impl.UserService;
import com.tutorial.springboot.rbac.test_utils.stub.DtoStubFactory;
import com.tutorial.springboot.rbac.test_utils.stub.TestDatabaseAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.rbac.test_utils.SecurityTestUtils.authenticateToTestEnv;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class UserServiceTest {

    @Autowired
    private UserService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @BeforeEach
    void setup() {
        authenticateToTestEnv();
    }

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

        @Test
        void givenNull_whenSaveOne_thenReturnNullPointerException() {
            final UserDto givenDto = null;

            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.save(givenDto));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
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

        @Test
        void givenNull_whenFindById_thenReturnNullPointerException() {
            final Long givenId = null;

            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.getById(givenId));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
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
                    .setEmail("newusername@host.com");
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.selectTestUser().entity().asOne();

            assertNotNull(actual);
            assertEquals("newusername", actual.getUsername());
            assertEquals("newpassword", actual.getPassword());
            assertEquals("newusername@host.com", actual.getEmail());
            assertTrue(actual.isEnabled());
        }

        @Test
        void givenNull_whenUpdate_thenReturnNullPointerException() {
            final UserDto givenDto = null;
            final Long givenId = null;

            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.update(givenId, givenDto));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
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

            systemUnderTest.deleteById(givenId);
            var actual = testDatabaseAssistant.selectTestUser().dto().asOne();

            assertNull(actual);
        }

        @Test
        void givenNull_whenDeleteById_thenReturnNullPointerException() {
            final Long givenId = null;

            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.deleteById(givenId));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }

    }

    @Nested
    class CustomMethodTests {

        @Test
        void givenUsername_whenFindByUsername_thenReturnUser() {
            var givenUser = testDatabaseAssistant.insertTestUserAndLogin().entity().asOne();
            var givenUserUsername = givenUser.getUsername();

            var actual = systemUnderTest.findByUsername(givenUserUsername);

            assertNotNull(actual);
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
        }

        @Test
        void givenOldPasswordAndNewPassword_whenChangePassword_thenUpdatePassword() {
            var givenUser = testDatabaseAssistant.insertTestUserAndLogin().entity().asOne();
            var givenUserPassword = givenUser.getPassword();

            systemUnderTest.changePassword(givenUserPassword, "updated_password");
            var actual = testDatabaseAssistant.selectTestUser().entity().asOne();

            assertNotNull(actual);
            assertFalse(actual.getPassword().isEmpty());
        }

        @Test
        void givenNull_whenChangePassword_thenReturnUser() {
            String givenOldPassword = null;
            String givenNewPassword = null;

            var actual = assertThrows(IllegalArgumentException.class, () -> systemUnderTest.changePassword(givenOldPassword, givenNewPassword));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isEmpty());
        }

        @Test
        void givenUser_whenFindByUsername_thenReturnUser() {
            String givenUserUsername = null;

            var actual = assertThrows(IllegalArgumentException.class, () -> systemUnderTest.findByUsername(givenUserUsername));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isEmpty());
        }
    }
}