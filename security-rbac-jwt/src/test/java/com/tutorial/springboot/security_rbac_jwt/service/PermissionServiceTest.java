package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PermissionService;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant.PermissionTestAssistant;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.PermissionTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils.login;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test", "h2"})
public class PermissionServiceTest {

    @Autowired
    private PermissionService systemUnderTest;


    @Autowired
    private PermissionTestAssistant assistant;

    @Autowired
    private PermissionTestFactory factory;

    @BeforeEach
    void setup() {
        login();
    }

    @Nested
    class SaveTests {

        @Test
        void givenDto_whenSaveOne_thenReturnId() {
            var givenDto = factory.newInstances(1).dto().asOne();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        void givenNull_whenSaveOne_thenReturnNullPointerException() {
            final PermissionDto givenDto = null;

            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.save(givenDto));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class FindTests {

        @Test
        void givenId_whenFindById_thenReturnDto() {
            var givenId = assistant.populate(1)
                    .dto()
                    .asOne()
                    .getId();

            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertFalse(actual.get().getName().isEmpty());
        }

        @Test
        void givenNull_whenFindById_thenReturnNullPointerException() {
            final Long givenId = null;

            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.getById(givenId));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class UpdateTests {

        @Test
        void givenUpdatedDto_whenUpdate_thenJustRunSuccessful() {
            var givenDto = assistant.populate(1)
                    .dto()
                    .asOne()
                    .setName("updated_value");
            var givenId = givenDto.getId();

            systemUnderTest.update(givenId, givenDto);
            var actual = assistant.select().dto().asOne();

            assertNotNull(actual);
            assertEquals("updated_value", actual.getName());
        }

        @Test
        void givenNull_whenUpdate_thenReturnNullPointerException() {
            final PermissionDto givenDto = null;
            final Long givenId = null;

            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.update(givenId, givenDto));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }

    @Nested
    class DeleteTests {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenId = assistant.populate(1)
                    .dto()
                    .asOne()
                    .getId();

            systemUnderTest.deleteById(givenId);
            var actual = assistant.select().dto().asOne();

            assertNull(actual);
        }

        @Test
        void givenNull_whenDeleteById_thenReturnNullPointerException() {
            final Long givenId = null;

            var actual = Assertions.assertThrows(NullPointerException.class, () -> systemUnderTest.deleteById(givenId));

            assertNotNull(actual);
            assertFalse(actual.getMessage().isBlank());
        }
    }
}
