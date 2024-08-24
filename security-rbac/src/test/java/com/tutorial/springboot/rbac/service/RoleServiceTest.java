package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.service.impl.RoleService;
import com.tutorial.springboot.rbac.test_utils.stub.DtoStubFactory;
import com.tutorial.springboot.rbac.test_utils.stub.TestDatabaseAssistant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.rbac.test_utils.TestUtils.loginByAdmin;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
public class RoleServiceTest {

    @Autowired
    RoleService systemUnderTest;

    @Autowired
    TestDatabaseAssistant testDatabaseAssistant;

    @BeforeEach
    void login() {
        loginByAdmin();
    }

    @Nested
    class SaveTests {

        @Test
        void givenDto_whenSaveOne_thenReturnId() {
            var givenDto = DtoStubFactory.createRole(1, 0).asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenRoleWithPermission_whenSaveOne_thenReturnId() {
            var givenDto = DtoStubFactory.createRole(1, 1).asOne();

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
            var givenId = testDatabaseAssistant.insertTestRole(1, 1)
                    .dto()
                    .asOne()
                    .getId();

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
            var givenDto = testDatabaseAssistant.insertTestRole(1, 1)
                    .dto()
                    .asOne()
                    .setName("updated_value");
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = testDatabaseAssistant.selectTestRole().dto().asOne();

            assertNotNull(actual);
            assertEquals("updated_value", actual.getName());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = testDatabaseAssistant.insertTestRole(1, 1)
                    .dto()
                    .asOne()
                    .getId();

            systemUnderTest.delete(givenId);
            var actual = testDatabaseAssistant.selectTestRole().dto().asOne();

            assertNull(actual);
        }
    }
}