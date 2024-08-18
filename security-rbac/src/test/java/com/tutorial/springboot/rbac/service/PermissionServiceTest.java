package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.fixture.DtoFixture;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import com.tutorial.springboot.rbac.service.impl.PermissionService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.rbac.fixture.Fixture.TEST_PERMISSION_NAME;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class PermissionServiceTest {

    @Autowired
    PermissionService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    class SaveTests {

        @Test
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = DtoFixture.createTestPermission();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFind_thenReturnDto() {
            var givenId = testDatabaseAssistant.newTestPermission().asDto.getId();

            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals(TEST_PERMISSION_NAME, actual.get().getName());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDto_whenUpdate_thenJustRunSuccessful() {
            var givenDto = testDatabaseAssistant.newTestPermission()
                    .asDto
                    .setName("UPDATED_PRIVILEGE");
            var givenId = givenDto.getId();


            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.fetchTestPermission().asDto;

            assertNotNull(actual);
            assertEquals("UPDATED_PRIVILEGE", actual.getName());
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        void givenId_whenDelete_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.newTestPermission().asDto.getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.fetchTestPermission().asDto;

            assertNull(actual);
        }

    }
}