package com.tutorial.springboot.rbac;

import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.RolePermission;
import com.tutorial.springboot.rbac.repository.PermissionRepository;
import com.tutorial.springboot.rbac.repository.RolePermissionRepository;
import com.tutorial.springboot.rbac.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
@DisplayName("Tests for CRUD operations in RolePermissionRepository")
public class RolePermissionRepositoryTest {

    @Autowired
    private RolePermissionRepository systemUnderTest;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PermissionRepository permissionRepository;

    static class EntityFixture {
        public static Permission createPermission(String name) {
            return new Permission()
                    .setName(name);
        }

        public static Role createRole(String authority) {
            return new Role()
                    .setAuthority(authority);
        }

        public static RolePermission createRelation(Role role, Permission permission) {
            return new RolePermission()
                    .setRole(role)
                    .setPermission(permission);

        }
    }

    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        private Role role;

        private Permission permission;

        @BeforeEach
        void setUp() {
            role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));
            permission = permissionRepository.save(EntityFixture.createPermission("READ_PRIVILEGE"));
        }

        @Test
        @DisplayName("Saving a RolePermission entity should persist it correctly.")
        void givenValidRolePermission_whenSave_thenEntityIsPersisted() {
            var givenRelation = EntityFixture.createRelation(role, permission);

            var actual = systemUnderTest.save(givenRelation);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(role.getId(), actual.getRole().getId());
            assertEquals(permission.getId(), actual.getPermission().getId());
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));
            var permission = permissionRepository.save(EntityFixture.createPermission("READ_PRIVILEGE"));
            var relation = systemUnderTest.save(EntityFixture.createRelation(role, permission));

            givenId = relation.getId();
        }

        @Test
        @DisplayName("Retrieving a RolePermission entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnRolePermission() {
            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));
            var permission = permissionRepository.save(EntityFixture.createPermission("READ_PRIVILEGE"));
            var relation = systemUnderTest.save(EntityFixture.createRelation(role, permission));

            givenId = relation.getId();
        }

        @Test
        @DisplayName("Updating a RolePermission entity should persist the changes.")
        void givenUpdatedRolePermission_whenUpdate_thenChangesPersisted() {
            var newRole = roleRepository.save(EntityFixture.createRole("ROLE_ADMIN"));
            systemUnderTest.findById(givenId)
                    .ifPresent(givenRelation -> {
                        givenRelation.setRole(newRole);
                        var actual = systemUnderTest.save(givenRelation);
                        assertEquals(newRole.getId(), actual.getRole().getId());
                    });
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var role = roleRepository.save(EntityFixture.createRole("ROLE_USER"));
            var permission = permissionRepository.save(EntityFixture.createPermission("READ_PRIVILEGE"));
            var relation = systemUnderTest.save(EntityFixture.createRelation(role, permission));

            givenId = relation.getId();
        }

        @Test
        @DisplayName("Deleting a RolePermission entity should remove it from the repository.")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}
