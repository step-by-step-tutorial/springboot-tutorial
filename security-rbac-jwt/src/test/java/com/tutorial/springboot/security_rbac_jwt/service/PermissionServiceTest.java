package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionDtoAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityAssertionUtils;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PermissionService;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionDtoFixture.newGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.findPermissionById;
import static com.tutorial.springboot.security_rbac_jwt.fixture.permission.PermissionEntityFixture.persistedGivenPermission;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestAuthenticationHelper.login;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class PermissionServiceTest {

    @Autowired
    private PermissionService systemUnderTest;

    @Autowired
    private EntityManagerFactory assistant;

    @BeforeEach
    void setup() {
        login();
    }

    @Nested
    class SaveOneTests {

        @Test
        void givenValidPermission_whenSave_thenReturnsValidId() {
            var givenPermission = newGivenPermission();

            var actual = systemUnderTest.save(givenPermission);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                var permission = findPermissionById(assistant, id);
                PermissionEntityAssertionUtils.assertPermission(permission, 1, 0);
            });
        }

        @Test
        void givenNullPermission_whenSave_thenThrowsNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.save(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenExistingId_whenFindById_thenReturnsPermission() {
            var givenId = persistedGivenPermission(assistant).getId();

            var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(dto -> PermissionDtoAssertionUtils.assertPermission(dto, 1, 0));
        }

        @Test
        void givenNullId_whenFindById_thenThrowsNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.findById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenValidIdAndUpdatedPermission_whenUpdate_thenSavesSuccessfully() {
            var givenId = persistedGivenPermission(assistant).getId();
            var givenPermission = newGivenPermission("updated_value");

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.update(givenId, givenPermission);
                return findPermissionById(assistant, givenId);
            });

            assertNotNull(actual);
            PermissionEntityAssertionUtils.assertPermission(actual, 1, 1);
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenNullIdOrPermission_whenUpdate_thenThrowsNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.update(null, null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenExistingId_whenDeleteById_thenRemovesSuccessfully() {
            var givenId = persistedGivenPermission(assistant).getId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return findPermissionById(assistant, givenId);
            });

            assertNull(actual);
        }

        @Test
        void givenNullId_whenDeleteById_thenThrowsNullPointerException() {
            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.deleteById(null));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

}
