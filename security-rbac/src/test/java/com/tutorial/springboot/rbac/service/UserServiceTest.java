package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.repository.UserRepository;
import com.tutorial.springboot.rbac.service.impl.UserService;
import com.tutorial.springboot.rbac.transformer.UserTransformer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
@DisplayName("Tests for CRUD operations of UserService")
public class UserServiceTest {

    @Autowired
    private UserRepository systemAssistant;

    @Autowired
    private UserTransformer userTransformer;

    @Autowired
    private UserService systemUnderTest;

    private static class Fixture {
        static User createEntity() {
            return new User()
                    .setUsername("john_doe")
                    .setPassword("password")
                    .setEmail("john@example.com")
                    .setEnabled(true);
        }

        static UserDto createDto() {
            return new UserDto()
                    .setUsername("john_doe")
                    .setPassword("password")
                    .setEmail("john@example.com")
                    .setEnabled(true);
        }
    }

    @Nested
    @DisplayName("Save Tests")
    class SaveTests {

        @Test
        @DisplayName("Given valid DTO, When creating user, Then user is saved in repository")
        @DirtiesContext
        void givenValidDto_whenSave_thenReturnID() {
            var givenDto = Fixture.createDto();

            var actual = systemUnderTest.save(givenDto);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertTrue(actual.get() > 0);
        }

        @Test
        @DisplayName("Given valid DTO, When creating user, Then user is saved in repository")
        @DirtiesContext
        void givenRoleWithPermission_whenSave_thenReturnID() {
            var givenRole = List.of(
                    new RoleDto().setAuthority("READ"),
                    new RoleDto().setAuthority("WRITE")
            );
            var givenDto = Fixture.createDto()
                    .setRoles(givenRole);

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
        @DisplayName("Given existing ID, When finding user, Then user is returned from repository")
        @DirtiesContext
        void givenExistingId_whenFind_thenReturnDto() {
            var actual = systemUnderTest.getById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertEquals("john_doe", actual.get().getUsername());
            assertEquals("john@example.com", actual.get().getEmail());
        }
    }

    @Nested
    @DisplayName("Update Tests")
    class UpdateTests {
        private User targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createEntity());
        }

        @Test
        @DisplayName("Given valid DTO, When updating user, Then user is updated in repository")
        @DirtiesContext
        void givenValidDto_whenUpdate_thenJustRunSuccessful() {
            var givenId = targetEntity.getId();
            var givenDto = userTransformer
                    .toDto(targetEntity)
                    .setEmail("john_new@example.com");

            systemUnderTest.update(givenId, givenDto);
            var actual = systemAssistant.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            assertThat(actual.get().getEmail()).isEqualTo("john_new@example.com");
        }
    }

    @Nested
    @DisplayName("Delete Tests")
    class DeleteTests {

        private User targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createEntity());
        }

        @Test
        @DisplayName("Given existing ID, When deleting user, Then user is removed from repository")
        @DirtiesContext
        void givenExistingId_whenDelete_thenJustRunSuccessful() {
            var givenId = targetEntity.getId();

            systemUnderTest.delete(givenId);
            var actual = systemAssistant.findById(givenId);

            assertNotNull(actual);
            assertFalse(actual.isPresent());
        }
    }

    @Nested
    @DisplayName("Custom Method Tests")
    class CustomMethodTests {
        private User targetEntity;

        @BeforeEach
        void init() {
            targetEntity = systemAssistant.save(Fixture.createEntity());

            var auth = new UsernamePasswordAuthenticationToken(targetEntity.getUsername(), targetEntity.getPassword());
            SecurityContextHolder.setContext(new SecurityContextImpl(auth));
        }

        @Test
        @DisplayName("Given username, When finding user by username, Then user is returned")
        @DirtiesContext
        void givenUsername_whenFindByUsername_thenReturnUser() {
            var actual = systemUnderTest.findByUsername("john_doe");

            assertNotNull(actual);
            assertEquals("john_doe", actual.getUsername());
            assertEquals("john@example.com", actual.getEmail());
        }

        @Test
        @DisplayName("Given old and new passwords, When changing password, Then password is updated")
        @DirtiesContext
        void givenOldPasswordAndNewPassword_whenChangePassword_thenUpdatePassword() {
            systemUnderTest.changePassword("password", "new_password");
            var actual = systemAssistant.findByUsername("john_doe").orElse(null);

            assertNotNull(actual);
            assertEquals("new_password", actual.getPassword());
        }

    }
}