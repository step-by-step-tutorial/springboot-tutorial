package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.fixture.DtoStubFactory;
import com.tutorial.springboot.rbac.fixture.StubFactory;
import com.tutorial.springboot.rbac.fixture.TestDatabaseAssistant;
import com.tutorial.springboot.rbac.service.impl.RoleService;
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
public class RoleServiceTest {

    @Autowired
    RoleService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    class SaveTests {

        @Test
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = (RoleDto)new DtoStubFactory()
                    .addRole()
                    .get()
                    .asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenRoleWithPermission_whenSave_thenReturnID() {
            var givenDto = new DtoStubFactory()
                    .addPermission()
                    .addRole()
                    .get()
                    .asOne();

            var actual = systemUnderTest.save((RoleDto) givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

    }

    @Nested
    class FindTests {

        @Test
        void givenExistingId_whenFind_thenReturnDto() {
            var givenId = testDatabaseAssistant.newTestRole().asDto.getId();

            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertFalse(actual.get().getName().isEmpty());
        }
    }

    @Nested
    class UpdateTests {

        @Test
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
    class DeleteTests {

        @Test
        void givenExistingId_whenDelete_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.newTestRole().asDto.getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.fetchTestRole().asDto;

            assertNull(actual);
        }
    }
}