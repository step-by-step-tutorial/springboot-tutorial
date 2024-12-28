package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleEntityAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.fixture.user.UserDtoAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.service.impl.UserService;
import com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.security_rbac_jwt.fixture.role.RoleDtoFixture.newGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserDtoFixture.newGivenUser;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityFixture.findUserById;
import static com.tutorial.springboot.security_rbac_jwt.fixture.user.UserEntityFixture.persistedGivenUser;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class UserServiceTest {

    @Autowired
    private UserService systemUnderTest;

    @Autowired
    private EntityManagerFactory assistant;

    @Autowired
    private TestAuthenticationHelper testAuthHelper;

    @BeforeEach
    void setup() {
        login();
    }

    @Nested
    class SaveTests {

        @Test
        void givenValidUser_thenReturnId() {
            var givenUser = newGivenUser();

            var actual = systemUnderTest.save(givenUser);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                var user = findUserById(assistant, id);
                UserEntityAssertionUtils.assertUser(user, 1, 0);
                assertNotNull(user.getRoles());
                assertTrue(user.getRoles().isEmpty());
            });

        }

        @Test
        void givenUserWithRoleAndPermission_thenReturnId() {
            var givenUser = newGivenUser(newGivenRole());

            var actual = systemUnderTest.save(givenUser);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                var user = findUserById(assistant, id);
                UserEntityAssertionUtils.assertUser(user, 1, 1);
                RoleEntityAssertionUtils.assertRoles(user.getRoles(), 1, new long[]{1}, new int[]{0});
            });
        }

        @Test
        void givenNullUser_thenThrowNullPointerException() {
            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.save(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class FindByIdTests {

        @Test
        void givenUserId_thenReturnUser() {
            var givenId = persistedGivenUser(assistant).getId();

            var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(dto -> UserDtoAssertionUtils.assertUser(dto, 1, 0));
        }

        @Test
        void givenNullId_thenThrowNullPointerException() {
            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.findById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUser_thenUpdateSuccessfully() {
            var givenId = persistedGivenUser(assistant).getId();
            var givenUser = newGivenUser();
            givenUser.setUsername("newusername");
            givenUser.setEmail("newusername@email.com");

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenUser);
                return findUserById(assistant, givenId);
            });

            UserEntityAssertionUtils.assertUser(actual, 1, 1);
            assertNotNull(actual.getRoles());
            assertTrue(actual.getRoles().isEmpty());
            assertEquals("newusername", actual.getUsername());
            assertEquals("newusername@email.com", actual.getEmail());
        }

        @Test
        void givenNullValues_thenThrowNullPointerException() {
            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.update(null, null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenUserId_thenCompleteSuccessfully() {
            var givenId = persistedGivenUser(assistant).getId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return findUserById(assistant, givenId);
            });

            assertNull(actual);
        }

        @Test
        void givenNullId_thenThrowNullPointerException() {
            var actual = assertThrows(NullPointerException.class, () -> systemUnderTest.deleteById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class CustomMethodTests {

        @Test
        void givenUsername_whenFindingByUsername_thenReturnUser() {
            var givenUser = testAuthHelper.signupAndLogin();
            var givenUserUsername = givenUser.getUsername();

            var actual = systemUnderTest.findByUsername(givenUserUsername);

            UserDtoAssertionUtils.assertUser(actual, 1, 0);
        }

        @Test
        void givenNull_whenFindingByUsername_thenThrowIllegalArgumentException() {
            var actual = assertThrows(IllegalArgumentException.class, () -> systemUnderTest.findByUsername(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isEmpty());
        }

        @Test
        void givenValidPassword_whenChangingPassword_thenUpdateSuccessfully() {
            var passwordEncoder = new BCryptPasswordEncoder();
            var user = testAuthHelper.signupAndLogin();
            var givenUsername = user.getUsername();
            var givenCurrentPassword = user.getPassword();
            var givenNewPassword = "updated_password";

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.changePassword(givenUsername, givenCurrentPassword, givenNewPassword);
                return findUserById(assistant, user.getId());
            });

            UserEntityAssertionUtils.assertUser(actual, 1, 1);
            passwordEncoder.matches(givenNewPassword, actual.getPassword());
        }

        @Test
        void givenNullValues_whenChangingPassword_thenThrowIllegalArgumentException() {
            var actual = assertThrows(IllegalArgumentException.class,
                    () -> systemUnderTest.changePassword(null, null, null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isEmpty());
        }
    }

}