package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.fixture.DtoFixture;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import com.tutorial.springboot.rbac.service.impl.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
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
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = DtoFixture.createTestUser();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenUserIncludeRoleWithPermission_whenSave_thenReturnID() {
            var givenDto = DtoFixture.createTestUserIncludeRoleAndPermission();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }


    }

    @Nested
    class FindTests {

        @Test
        void givenExistingId_whenFind_thenReturnDto() {
            var givenId = testDatabaseAssistant.newTestUser().asDto.getId();
            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals("test", actual.get().getUsername());
            assertEquals("test@example.com", actual.get().getEmail());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenValidDto_whenUpdate_thenJustRunSuccessful() {
            var givenDto = testDatabaseAssistant.newTestUserIncludeRoleAndPermission()
                    .asDto
                    .setEmail("updatedtest@example.com");
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.fetchTestUser().asDto;

            assertNotNull(actual);
            assertEquals("updatedtest@example.com", actual.getEmail());
        }
    }

    @Nested
    class DeleteTests {


        @Test
        void givenExistingId_whenDelete_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.newTestUser().asDto.getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.fetchTestUser().asDto;

            assertNull(actual);
        }
    }

    @Nested
    class CustomMethodTests {

        @BeforeEach
        void init() {
            var user = testDatabaseAssistant.newTestUser().asDto;

            var auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
            SecurityContextHolder.setContext(new SecurityContextImpl(auth));
        }

        @Test
        void givenUsername_whenFindByUsername_thenReturnUser() {
            var actual = systemUnderTest.findByUsername("test");

            assertNotNull(actual);
            assertEquals("test", actual.getUsername());
            assertEquals("test@example.com", actual.getEmail());
        }

        @Test
        void givenOldPasswordAndNewPassword_whenChangePassword_thenUpdatePassword() {
            systemUnderTest.changePassword("test", "new_test");
            var actual = testDatabaseAssistant.fetchTestUser().asDto;

            assertNotNull(actual);
            assertEquals("new_test", actual.getPassword());
        }

    }
}