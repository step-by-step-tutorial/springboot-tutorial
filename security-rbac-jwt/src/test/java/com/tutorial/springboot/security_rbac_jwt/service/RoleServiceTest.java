package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.service.impl.RoleService;
import com.tutorial.springboot.security_rbac_jwt.testutils.DtoAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.testutils.EntityAssertionUtils;
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

    @Nested
    class SaveTests {

        @Test
        void givenValidRole_whenSaved_thenReturnValidId() {
            var givenRole = newGivenRole();

            var actual = systemUnderTest.save(givenRole);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                var role = findRoleById(assistant, id);
                EntityAssertionUtils.assertRole(role, 1, 0);
                assertNotNull(role.getPermissions());
                assertTrue(role.getPermissions().isEmpty());
            });
        }

        @Test
        void givenRoleWithPermissions_whenSaved_thenReturnValidId() {
            var givenRole = newGivenRole(newGivenPermission());

            var actual = systemUnderTest.save(givenRole);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                var role = findRoleById(assistant, id);
                EntityAssertionUtils.assertRole(role, 1, 1);
                EntityAssertionUtils.assertPermissions(role.getPermissions(), 1, new long[]{1}, new int[]{0});
            });
        }

        @Test
        void givenNullInput_whenSaved_thenThrowNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.save(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenExistingId_whenFindById_thenReturnRole() {
            var givenId = EntityFixture.persistedGivenRole(assistant).getId();

            var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(dto -> {
                DtoAssertionUtils.assertRole(dto, 1, 0);
            });
        }

        @Test
        void givenNullId_whenFindById_thenThrowNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.findById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenExistingRoleWithUpdates_whenUpdated_thenFieldValuesAreUpdated() {
            var givenId = EntityFixture.persistedGivenRole(assistant).getId();
            var givenRole = newGivenRole("updated_value");

            systemUnderTest.update(givenId, givenRole);
            var actual = findRoleById(assistant, givenId);

            assertNotNull(actual);
            EntityAssertionUtils.assertRole(actual, 1, 1);
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenNullIdAndRole_whenUpdated_thenThrowNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.update(null, null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenValidId_whenDeletedById_thenRoleIsDeleted() {
            var givenId = EntityFixture.persistedGivenRole(assistant).getId();

            systemUnderTest.deleteById(givenId);
            var actual = findRoleById(assistant, givenId);

            assertNull(actual);
        }

        @Test
        void givenNullId_whenDeletedById_thenThrowNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.deleteById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    private Role findRoleById(EntityManagerFactory emf, Long id) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();
        transaction.begin();
        var role = em.find(Role.class, id);
        em.flush();
        em.clear();
        transaction.commit();
        return role;
    }
}