package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant.UserTestAssistant;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils.login;
import static java.time.LocalDateTime.now;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
@ActiveProfiles(value = {"test", "h2"})
public class UserRepositoryTest {

    @Autowired
    private UserRepository systemUnderTest;

    @Autowired
    private EntityManager em;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserTestAssistant assistant;

    @Nested
    class SaveTest {

        @Test
        void givenNewUser_whenSave_thenReturnPersistedUser() {
            var givenUser = newGivenUser();

            var actual = systemUnderTest.save(givenUser);
            em.flush();

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertTrue(actual.getId() > 0);
            assertEquals(0, (int) actual.getVersion());
        }

        @Test
        void givenNewUserWithRole_whenSave_thenReturnPersistedUser() {
            var givenRole = newGivenRole();
            var givenUser = newGivenUser().setRoles(List.of(givenRole));

            var actual = systemUnderTest.save(givenUser);
            em.flush();

            // assert user
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(1, (int) actual.getVersion());

            // assert role
            assertNotNull(actual.getRoles());
            assertEquals(1, actual.getRoles().size());
            assertTrue(actual.getRoles().getFirst().getId() > 0);
            assertEquals(0, (int) actual.getRoles().getFirst().getVersion());
        }

        @Test
        void givenNewUserWithRoleAndPermission_whenSave_thenReturnPersistedUser() {
            var permission = newGivenPermission();
            var givenRole = newGivenRole().setPermissions(List.of(permission));
            var givenUser = newGivenUser().setRoles(List.of(givenRole));

            var actual = systemUnderTest.save(givenUser);
            em.flush();

            // assert user
            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(1, (int) actual.getVersion());

            // assert role
            assertNotNull(actual.getRoles());
            assertEquals(1, actual.getRoles().size());
            assertTrue(actual.getRoles().getFirst().getId() > 0);
            assertEquals(1, (int) actual.getRoles().getFirst().getVersion());

            // assert permission
            assertNotNull(actual.getRoles().getFirst().getPermissions());
            assertEquals(1, actual.getRoles().getFirst().getPermissions().size());
            assertTrue(actual.getRoles().getFirst().getPermissions().getFirst().getId() > 0);
            assertEquals(0, (int) actual.getRoles().getFirst().getPermissions().getFirst().getVersion());
        }
    }

    @Nested
    class SaveAllTest {

        @Test
        void givenListOfUser_whenSaveAll_thenReturnListOfPersistedUser() {
            var givenUsers = List.of(newGivenUser("username1"), newGivenUser("username2"));

            var actual = systemUnderTest.saveAll(givenUsers);
            em.flush();

            assertEquals(2, actual.size());
            actual.forEach(actualItem -> {
                assertNotNull(actualItem.getId());
                assertTrue(actualItem.getId() > 0);
                assertEquals(0, (int) actualItem.getVersion());
            });
        }
    }

    @Nested
    class FindTest {

        @Test
        void givenId_whenFindById_thenReturnUser() {
            var givenUser = newGivenUser();
            em.persist(givenUser);
            em.flush();
            em.clear();
            var givenId = givenUser.getId();

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals(givenId, actual.get().getId());
            assertFalse(actual.get().getUsername().isEmpty());
            assertFalse(actual.get().getPassword().isEmpty());
            assertFalse(actual.get().getEmail().isEmpty());
            assertTrue(actual.get().isEnabled());
        }
    }

    @Nested
    class UpdateTest {

        @Test
        void givenUpdatedUser_whenUpdate_thenReturnPersistedUser() {
            login();
            var user = newGivenUser();
            em.persist(user);
            em.flush();
            em.clear();
            em.detach(user);
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            var givenUser = newGivenUser("newusername");
            var toUpdate = em.find(User.class, givenId);
            toUpdate.updateFrom(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion + 1, actual.getVersion());
            assertEquals("newusername", actual.getUsername());
        }

        @Test
        void givenUserWithUpdatedRoles_whenUpdate_thenReturnPersistedUser() {
            login();
            var role = newGivenRole("guest");
            var user = newGivenUser().setRoles(List.of(role));
            em.persist(user);
            em.flush();
            em.clear();
            em.detach(user);
            var roleId = role.getId();
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            role = em.find(Role.class, roleId);
            var givenRole = newGivenRole("host");
            var givenUser = newGivenUser().setRoles(List.of(role, givenRole)) ;
            var toUpdate = em.find(User.class, givenId);
            toUpdate.updateRelations(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion, actual.getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(0, actual.getPermissions().size());
            assertEquals(2, actual.getRoles().size());
            assertEquals("guest", actual.getRoles().getFirst().getName());
            assertEquals(0, actual.getRoles().getFirst().getVersion());
            assertEquals("host", actual.getRoles().getLast().getName());
            assertEquals(0, actual.getRoles().getLast().getVersion());

        }

        @Test
        void givenUserWithRolesAndUpdatedPermissions_whenUpdate_thenReturnPersistedUser() {
            login();
            var permission = newGivenPermission("read");
            var role = newGivenRole("guest").setPermissions(List.of(permission)) ;
            var user = newGivenUser().setRoles(List.of(role));
            em.persist(user);
            em.flush();
            em.clear();
            var permissionId = permission.getId();
            var roleId = role.getId();
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            permission = em.find(Permission.class, permissionId);
            var givenPermission = newGivenPermission("write");
            role = em.find(Role.class, roleId);
            role.setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenUser = newGivenUser().setRoles(List.of(role)) ;
            var toUpdate = em.find(User.class, givenId);
            toUpdate.updateRelations(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion, actual.getVersion());

            assertEquals(1, actual.getRoles().size());
            assertEquals("guest", actual.getRoles().getFirst().getName());
            assertEquals(0, actual.getRoles().getFirst().getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(2, actual.getPermissions().size());
        }

        @Test
        void givenUserWithUpdatedRolesAndPermissions_whenUpdate_thenReturnPersistedUser() {
            login();
            var permission = newGivenPermission("read");
            var role = newGivenRole("guest").setPermissions(List.of(permission)) ;
            var user = newGivenUser().setRoles(List.of(role));
            em.persist(user);
            em.flush();
            em.clear();
            var permissionId = permission.getId();
            var roleId = role.getId();
            var givenId = user.getId();
            var givenVersion = user.getVersion();

            permission = em.find(Permission.class, permissionId);
            var givenPermission = newGivenPermission("write");
            role = em.find(Role.class, roleId);
            role.setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenRole = newGivenRole("host").setPermissions(new ArrayList<>(List.of(permission, givenPermission)));
            var givenUser = newGivenUser().setRoles(new ArrayList<>(List.of(role, givenRole)) );
            var toUpdate = em.find(User.class, givenId);
            toUpdate.updateRelations(givenUser);

            var actual = systemUnderTest.save(toUpdate);

            assertNotNull(actual);
            assertEquals(user.getId(), actual.getId());
            assertEquals(givenVersion, actual.getVersion());

            assertEquals(2, actual.getRoles().size());
            assertEquals("guest", actual.getRoles().getFirst().getName());
            assertEquals(0, actual.getRoles().getFirst().getVersion());
            assertEquals("host", actual.getRoles().getLast().getName());
            assertEquals(0, actual.getRoles().getLast().getVersion());

            assertNotNull(actual.getPermissions());
            assertEquals(2, actual.getPermissions().size());
        }
    }

    @Nested
    class DeleteTest {

        @Test
        void givenId_whenDeleteById_thenJustRunSuccessful() {
            var givenUser = newGivenUser();
            em.persist(givenUser);
            em.flush();
            em.clear();
            var givenId = givenUser.getId();

            var actual = assertDoesNotThrow(() -> {
                systemUnderTest.deleteById(givenId);
                return systemUnderTest.findById(givenId);
            });

            assertFalse(actual.isPresent());
        }
    }

    private User newGivenUser() {
        return new User()
                .setUsername("username").setPassword("password").setEmail("username@email.com").setEnabled(true)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private User newGivenUser(String username) {
        Objects.requireNonNull(username);
        return new User()
                .setUsername(username).setPassword("password").setEmail(username + "@email.com").setEnabled(true)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Role newGivenRole() {
        return new Role()
                .setName("role")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Role newGivenRole(String name) {
        return new Role()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Permission newGivenPermission() {
        return new Permission()
                .setName("permission")
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

    private Permission newGivenPermission(String name) {
        return new Permission()
                .setName(name)
                .setCreatedBy("unittest").setCreatedAt(now())
                .setVersion(0);
    }

}