package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.test_utils.stub.TransientStubFactory;
import com.tutorial.springboot.rbac.test_utils.assistant.TestDatabaseAssistant;
import com.tutorial.springboot.rbac.service.impl.PermissionService;
import org.junit.jupiter.api.DisplayName;
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
public class PermissionServiceTest {

    @Autowired
    PermissionService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @Nested
    class SaveTests {

        @Test
        void givenValidDto_whenSaveOne_thenReturnId() {
            var givenDto = TransientStubFactory.createPermission(1).asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindById_thenReturnDto() {
            var givenId = testDatabaseAssistant.insertTestPermission().asDto.getId();

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
            var givenDto = testDatabaseAssistant.insertTestPermission()
                    .asDto
                    .setName("UPDATED_PRIVILEGE");
            var givenId = givenDto.getId();


            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.selectTestPermission().asDto;

            assertNotNull(actual);
            assertEquals("UPDATED_PRIVILEGE", actual.getName());
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestPermission().asDto.getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.selectTestPermission().asDto;

            assertNull(actual);
        }

    }
}