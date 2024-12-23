package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.service.impl.RoleService;
import com.tutorial.springboot.security_rbac_jwt.testutils.EntityFixture;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.testutils.DtoFixture.newGivenRole;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class RoleServiceTest {

    @Autowired
    private RoleService systemUnderTest;

    @Autowired
    private EntityManagerFactory assistant;

    @BeforeEach
    void setup() {
        login();
    }

    private Long newGivenId() {
        var em = assistant.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var role = EntityFixture.newGivenRole();
        em.persist(role);
        em.flush();
        em.clear();
        transaction.commit();

        return role.getId();
    }

    @Nested
    class SaveTests {

        @Test
        void givenRole_whenSaveOne_thenReturnId() {
            var givenRole = newGivenRole();

            var actual = systemUnderTest.save(givenRole);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenRoleWithPermission_whenSaveOne_thenReturnId() {
            var givenPermission = newGivenPermission();
            var givenRole = newGivenRole().setPermissions(List.of(givenPermission));

            var actual = systemUnderTest.save(givenRole);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenNull_whenSaveOne_thenReturnNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.save(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindById_thenReturnRole() {
            var givenId = newGivenId();

            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertFalse(actual.get().getName().isEmpty());
        }

        @Test
        void givenNull_whenFindById_thenReturnNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.getById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedRole_whenUpdate_thenJustRunSuccessful() {
            var givenId = newGivenId();
            var givenRole = newGivenRole().setName("updated_value");

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenRole);
                return systemUnderTest.getById(givenId).orElseThrow();
            });

            assertNotNull(actual);
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenNull_whenUpdate_thenReturnNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.update(null, null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = newGivenId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.getById(givenId);
            });

            assertNotNull(actual);
            assertFalse(actual.isPresent());
        }

        @Test
        void givenNull_whenDeleteById_thenReturnNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.deleteById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }

    }
}