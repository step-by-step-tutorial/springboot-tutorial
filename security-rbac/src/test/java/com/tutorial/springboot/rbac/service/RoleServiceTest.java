package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.fixture.Fixture;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import com.tutorial.springboot.rbac.service.impl.RoleService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.rbac.fixture.DtoFixture.createMultiTestPermission;
import static com.tutorial.springboot.rbac.fixture.DtoFixture.createTestRole;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("Tests for CRUD operations of RoleService")
public class RoleServiceTest {

    @Autowired
    RoleService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Given valid DTO, When creating role, Then role is saved in repository")
        @DirtiesContext
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = createTestRole();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        @DisplayName("Given valid DTO with list of Permission, When creating role, Then role is saved in repository")
        @DirtiesContext
        void givenRoleWithPermission_whenSave_thenReturnID() {
            var givenDto = createTestRole()
                    .setPermissions(createMultiTestPermission(2));

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

    }

    @Nested
    @DisplayName("Find Tests")
    class FindTests {

        @Test
        @DisplayName("Given existing ID, When finding role, Then role is returned from repository")
        @DirtiesContext
        void givenExistingId_whenFind_thenReturnDto() {
            var givenId = testDatabaseAssistant.newTestRole().asDto.getId();

            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals(Fixture.TEST_ROLE_NAME, actual.get().getName());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {

        @Test
        @DisplayName("Given valid DTO, When updating role, Then role is updated in repository")
        @DirtiesContext
        void givenValidDto_whenUpdate_thenJustRunSuccessful() {
            var givenDto = testDatabaseAssistant.newTestRoleIncludePermission()
                    .asDto
                    .setName("Updated Role");
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.fetchTestRole().asDto;

            assertNotNull(actual);
            assertEquals("Updated Role", actual.getName());
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        @DisplayName("Given existing ID, When deleting role, Then role is removed from repository")
        @DirtiesContext
        void givenExistingId_whenDelete_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.newTestRole().asDto.getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.fetchTestRole().asDto;

            assertNull(actual);
        }
    }
}