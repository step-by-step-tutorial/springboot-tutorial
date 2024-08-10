package com.tutorial.springboot.rbac;

import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.repository.RoleRepository;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
@DisplayName("Tests for CRUD operations and validations in RoleRepository")
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository systemUnderTest;

    static class EntityFixture {
        public static Role createOne(String authority) {
            return new Role().setAuthority(authority);
        }

        public static List<Role> createMulti(String... authorities) {
            return Arrays.stream(authorities)
                    .map(EntityFixture::createOne)
                    .toList();
        }
    }

    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        @Test
        @DisplayName("Saving a valid Role entity should persist it and generate an ID.")
        void givenValidEntity_whenSave_thenReturnID() {
            var givenEntity = EntityFixture.createOne("ROLE_USER");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals("ROLE_USER", actual.getAuthority());
        }

        @Test
        @DisplayName("Saving multiple Role entities should persist all and return correct count.")
        void givenValidEntities_whenSaveAll_thenReturnSavedEntities() {
            var entities = EntityFixture.createMulti("ROLE_USER", "ROLE_ADMIN", "ROLE_MANAGER");

            var actual = systemUnderTest.saveAll(entities);

            assertNotNull(actual);
            assertEquals(3, actual.size());
            assertTrue(actual.stream().allMatch(entity -> entity.getId() != null));
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var entity = EntityFixture.createOne("ROLE_USER");
            systemUnderTest.save(entity);
            givenId = entity.getId();
        }

        @Test
        @DisplayName("Retrieving a Role entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnEntity() {
            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals("ROLE_USER", actual.get().getAuthority());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        private Role givenEntity;

        @BeforeEach
        void setUp() {
            givenEntity = EntityFixture.createOne("ROLE_USER");
            systemUnderTest.save(givenEntity);
        }

        @Test
        @DisplayName("Updating a saved Role entity should modify its properties.")
        void givenUpdatedEntity_whenUpdate_thenEntityIsUpdated() {
            givenEntity.setAuthority("ROLE_UPDATED");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals("ROLE_UPDATED", actual.getAuthority());
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var entity = EntityFixture.createOne("ROLE_USER");
            systemUnderTest.save(entity);
            givenId = entity.getId();
        }

        @Test
        @DisplayName("Deleting a Role entity by ID should remove it from the repository.")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTest {

        @ParameterizedTest
        @ValueSource(strings = {"", "  ", "A very long string that exceeds the maximum length for an authority"})
        @DisplayName("Saving a Role with an invalid authority should throw a ConstraintViolationException.")
        void givenInvalidAuthorities_whenSave_thenThrownConstraintViolationException(String givenAuthority) {
            var givenEntity = EntityFixture.createOne(givenAuthority);

            assertThrows(ConstraintViolationException.class, () -> systemUnderTest.saveAndFlush(givenEntity));
        }

        @Test
        @DisplayName("Saving a Role with a null authority should throw a ConstraintViolationException.")
        void givenNullAuthority_whenSave_thenThrownConstraintViolationException() {
            var givenEntity = EntityFixture.createOne(null);

            assertThrows(ConstraintViolationException.class, () -> systemUnderTest.saveAndFlush(givenEntity));
        }

        @Test
        @DisplayName("Saving a Role with a duplicate authority should throw a DataIntegrityViolationException.")
        void givenDuplicateAuthority_whenSave_thenThrownDataIntegrityViolationException() {
            var givenUniqueEntity = EntityFixture.createOne("ROLE_DUPLICATE");
            systemUnderTest.save(givenUniqueEntity);

            var givenDuplicatedEntity = EntityFixture.createOne("ROLE_DUPLICATE");

            assertThrows(DataIntegrityViolationException.class, () -> systemUnderTest.saveAndFlush(givenDuplicatedEntity));
        }
    }
}
