package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.repository.RoleRepository;
import com.tutorial.springboot.rbac.service.impl.RoleService;
import com.tutorial.springboot.rbac.transformer.RoleTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("Tests for CRUD operations of RoleService")
public class RoleServiceTest {

    @Autowired
    private RoleRepository systemAssistant;

    @Autowired
    private RoleTransformer roleTransformer;

    @Autowired
    private RoleService systemUnderTest;

    private static class Fixture {
        static Role createEntity() {
            return new Role()
                    .setName("ADMIN");
        }

        static RoleDto createDto() {
            return new RoleDto()
                    .setName("ADMIN");
        }
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Given valid DTO, When creating role, Then role is saved in repository")
        @DirtiesContext
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = Fixture.createDto();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        @DisplayName("Given valid DTO with list of Permission, When creating role, Then role is saved in repository")
        @DirtiesContext
        void givenRoleWithPermission_whenSave_thenReturnID() {
            var givenPermissions = List.of(
                    new PermissionDto().setName("READ"),
                    new PermissionDto().setName("WRITE")
            );
            var givenDto = Fixture.createDto()
                    .setPermissions(givenPermissions);

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
            givenId = systemAssistant.save(Fixture.createEntity()).getId();
        }

        @Test
        @DisplayName("Given existing ID, When finding role, Then role is returned from repository")
        @DirtiesContext
        void givenExistingId_whenFind_thenReturnDto() {
            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals("ADMIN", actual.get().getName());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {
        private Role targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createEntity()
                    .setPermissions(List.of(
                            new Permission().setName("READ"),
                            new Permission().setName("WRITE")
                    )));
        }

        @Test
        @DisplayName("Given valid DTO, When updating role, Then role is updated in repository")
        @DirtiesContext
        void givenValidDto_whenUpdate_thenJustRunSuccessful() {
            var givenId = targetEntity.getId();
            var givenDto = roleTransformer.toDto(targetEntity);
            givenDto.setName("USER");

            systemUnderTest.update(givenId, givenDto);
            var actual = systemAssistant.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertThat(actual.get().getName()).isEqualTo("USER");
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        private Role targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createEntity());
        }

        @Test
        @DisplayName("Given existing ID, When deleting role, Then role is removed from repository")
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