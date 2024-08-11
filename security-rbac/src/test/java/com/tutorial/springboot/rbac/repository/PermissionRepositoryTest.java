package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.Permission;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles(value = {"test"})
@DisplayName("Tests for CRUD operationsof PermissionRepository")
public class PermissionRepositoryTest {

    @Autowired
    private PermissionRepository systemUnderTest;

    static class EntityFixture {
        public static Permission createOne(String name) {
            return new Permission()
                    .setName(name);
        }

        public static List<Permission> createMulti(String... names) {
            return Arrays.stream(names)
                    .map(EntityFixture::createOne)
                    .toList();
        }
    }

    @Nested
    @DisplayName("Create Operation Tests")
    class CreateTest {

        @Test
        @DisplayName("Saving a valid Permission entity should persist it and generate an ID.")
        void givenValidEntity_whenSave_thenReturnID() {
            var givenEntity = EntityFixture.createOne("CREATE");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals("CREATE", actual.getName());
        }

        @Test
        @DisplayName("Saving multiple Permission entities should persist all and return correct count.")
        void givenValidEntities_whenSaveAll_thenReturnSavedEntities() {
            var entities = EntityFixture.createMulti("CREATE", "READ", "UPDATE", "DELETE");

            var actual = systemUnderTest.saveAll(entities);

            assertNotNull(actual);
            assertEquals(4, actual.size());
            assertTrue(actual.stream().allMatch(entity -> entity.getId() != null));
        }
    }

    @Nested
    @DisplayName("Read Operation Tests")
    class ReadTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var entity = EntityFixture.createOne("READ");
            systemUnderTest.save(entity);
            givenId = entity.getId();
        }

        @Test
        @DisplayName("Retrieving a Permission entity by ID should return the correct entity.")
        void givenID_whenFindById_thenReturnEntity() {
            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            assertEquals("READ", actual.get().getName());
        }
    }

    @Nested
    @DisplayName("Update Operation Tests")
    class UpdateTest {

        private Permission givenEntity;

        @BeforeEach
        void setUp() {
            givenEntity = EntityFixture.createOne("UPDATE");
            systemUnderTest.save(givenEntity);
        }

        @Test
        @DisplayName("Updating a saved Permission entity should modify its properties.")
        void givenUpdatedEntity_whenUpdate_thenEntityIsUpdated() {
            givenEntity.setName("UPDATED");

            var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals("UPDATED", actual.getName());
        }
    }

    @Nested
    @DisplayName("Delete Operation Tests")
    class DeleteTest {

        private Long givenId;

        @BeforeEach
        void setUp() {
            var entity = EntityFixture.createOne("READ");
            systemUnderTest.save(entity);
            givenId = entity.getId();
        }

        @Test
        @DisplayName("Deleting a Permission entity by ID should remove it from the repository.")
        void givenID_whenDeleteById_thenEntityIsDeleted() {
            systemUnderTest.deleteById(givenId);
            var actual = systemUnderTest.findById(givenId);

            assertFalse(actual.isPresent());
        }
    }
}
