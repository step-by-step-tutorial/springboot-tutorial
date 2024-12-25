package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.service.impl.UserService;
import com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture;
import com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenUser;
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

    private Long newGivenId() {
        var em = assistant.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var user = EntityFixture.newGivenUser();
        em.persist(user);
        em.flush();
        em.clear();
        transaction.commit();

        return user.getId();
    }

    @Nested
    class SaveTests {

        @Test
        void givenValidUser_thenReturnId() {
            var givenUser = newGivenUser();

            var actual = systemUnderTest.save(givenUser);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenUserWithRoleAndPermission_thenReturnId() {
            var givenRole = newGivenRole();
            var givenUser = newGivenUser();
            givenUser.getRoles().add(givenRole);

            var actual = systemUnderTest.save(givenUser);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
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
            var givenId = newGivenId();

            var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertFalse(actual.get().getUsername().isEmpty());
            assertFalse(actual.get().getEmail().isEmpty());
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
            var givenId = newGivenId();
            var givenUser = newGivenUser();
            givenUser.setUsername("newusername");
            givenUser.setEmail("newusername@email.com");

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenUser);
                return systemUnderTest.findById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals("newusername", actual.getUsername());
            assertEquals("newusername@email.com", actual.getEmail());
            assertTrue(actual.isEnabled());
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
            var givenId = newGivenId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.findById(givenId);
            });

            assertNotNull(actual);
            assertFalse(actual.isPresent());
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

            assertNotNull(actual);
            assertFalse(actual.getUsername().isEmpty());
            assertFalse(actual.getEmail().isEmpty());
        }

        @Test
        void givenNull_whenFindingByUsername_thenThrowIllegalArgumentException() {
            var actual = assertThrows(IllegalArgumentException.class, () -> systemUnderTest.findByUsername(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isEmpty());
        }

        @Test
        void givenValidPassword_whenChangingPassword_thenUpdateSuccessfully() {
            var givenUser = testAuthHelper.signupAndLogin();
            var givenUserPassword = givenUser.getPassword();

            var actual = assertDoesNotThrow(() -> {

                systemUnderTest.changePassword(givenUserPassword, "updated_password");
                var em = assistant.createEntityManager();
                var transaction = em.getTransaction();
                transaction.begin();
                var user = em.find(User.class, givenUser.getId());
                transaction.commit();
                em.close();
                return user;
            });

            assertNotNull(actual);
            assertFalse(actual.getPassword().isEmpty());
        }

        @Test
        void givenNullValues_whenChangingPassword_thenThrowIllegalArgumentException() {
            var actual = assertThrows(IllegalArgumentException.class,
                    () -> systemUnderTest.changePassword(null, null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isEmpty());
        }
    }
}