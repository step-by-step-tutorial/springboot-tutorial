package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.test_utils.stub.TransientStubFactory;
import com.tutorial.springboot.rbac.test_utils.assistant.TestDatabaseAssistant;
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
        void givenValidDto_whenSaveOne_thenReturnId() {
            var givenDto = TransientStubFactory.createRole(1, 0).asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenRoleWithPermission_whenSaveOne_thenReturnId() {
            var givenDto = TransientStubFactory.createRole(1, 1).asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

    }

    @Nested
    class FindTests {

        @Test
        void givenExistingId_whenFindById_thenReturnDto() {
            var givenId = testDatabaseAssistant.insertTestRole().asDto.getId();

            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertFalse(actual.get().getName().isEmpty());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDto_whenUpdate_thenJustRunSuccessful() {
            var givenDto = testDatabaseAssistant.insertComplexTestRole()
                    .asDto
                    .setName("Updated_Role");
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.selectTestRole().asDto;

            assertNotNull(actual);
            assertEquals("Updated_Role", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestRole().asDto.getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.selectTestRole().asDto;

            assertNull(actual);
        }
    }
}