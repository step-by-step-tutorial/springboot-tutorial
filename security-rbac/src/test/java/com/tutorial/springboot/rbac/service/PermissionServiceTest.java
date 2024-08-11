package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.repository.PermissionRepository;
import com.tutorial.springboot.rbac.service.impl.PermissionService;
import com.tutorial.springboot.rbac.transformer.PermissionTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("Tests for CRUD operationsof PermissionService")
public class PermissionServiceTest {

    @Autowired
    private PermissionRepository systemAssistant;

    @Autowired
    private PermissionTransformer permissionTransformer;

    @Autowired
    private PermissionService systemUnderTest;

    private static class Fixture {
        static Permission createPermissionEntity(String name) {
            return new Permission()
                    .setName(name);
        }

        static PermissionDto createPermissionDto(String name) {
            return new PermissionDto()
                    .setName(name);
        }
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Given valid DTO, When creating permission, Then permission is saved in repository")
        @DirtiesContext
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = Fixture.createPermissionDto("READ");

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }
    }

    @Nested
    @DisplayName("Find Tests")
    class FindTests {
        private Long givenId;

        @BeforeEach
        void init() {
            givenId = systemAssistant.save(Fixture.createPermissionEntity("READ")).getId();
        }

        @Test
        @DisplayName("Given existing ID, When finding permission, Then permission is returned from repository")
        @DirtiesContext
        void givenExistingId_whenFind_thenReturnDto() {
            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals("READ", actual.get().getName());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {
        private Permission targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createPermissionEntity("READ"));
        }

        @Test
        @DisplayName("Given valid DTO, When updating permission, Then permission is updated in repository")
        @DirtiesContext
        void givenValidDto_whenUpdate_thenJustRunSuccessful() {
            var givenId = targetEntity.getId();
            var givenDto = permissionTransformer
                    .toDto(targetEntity)
                    .setName("WRITE");

            systemUnderTest.update(givenId, givenDto);
            var actual = systemAssistant.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertThat(actual.get().getName()).isEqualTo("WRITE");
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        private Permission targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createPermissionEntity("READ"));
        }

        @Test
        @DisplayName("Given existing ID, When deleting permission, Then permission is removed from repository")
        @DirtiesContext
        void givenExistingId_whenDelete_thenJustRunSuccessful() {
            var givenId = targetEntity.getId();

            systemUnderTest.delete(givenId);
            var actual = systemAssistant.findById(givenId);

            assertNotNull(actual);
            assertFalse(actual.isPresent());
        }

    }
}