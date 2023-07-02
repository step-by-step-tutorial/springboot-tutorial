package com.tutorial.springboot.rdbms_oracle;

import com.tutorial.springboot.rdbms_oracle.entity.SampleEntity;
import com.tutorial.springboot.rdbms_oracle.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.OracleContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test"})
@DisplayName("unit tests of sample repository")
class SampleRepositoryTest {

    @Container
    static OracleContainer oracleContainer = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart")
            .withPassword("password");

    @SuppressWarnings({"unused"})
    @DynamicPropertySource
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", oracleContainer::getJdbcUrl);
        registry.add("spring.datasource.username", oracleContainer::getUsername);
        registry.add("spring.datasource.password", oracleContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", oracleContainer::getDriverClassName);
    }

    @Autowired
    SampleRepository systemUnderTest;

    @BeforeAll
    static void beforeAll() {
        oracleContainer.start();
    }

    @AfterAll
    static void afterAll() {
        oracleContainer.stop();
    }

    /**
     * This class includes Stub data.
     */
    static class StubFactory {
        static final LocalDateTime NOW = LocalDateTime.now();
        static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
        static final SampleEntity SAMPLE_ENTITY = SampleEntity.create()
                .setName("stub name")
                .setCode(1)
                .setDatetime(NOW);

    }

    @Nested
    @DisplayName("save nested unit tests")
    class SaveTest {

        @Test
        @DisplayName("save an entity")
        void shouldReturnEntityWithIdBySuccessfulSave() {
            final var givenEntity = StubFactory.SAMPLE_ENTITY;

            final var expectedId = 1L;
            final var expectedName = "stub name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFactory.NOW;

            final var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(expectedId, actual.getId());
            assertEquals(expectedName, actual.getName());
            assertEquals(expectedCode, actual.getCode());
            assertEquals(expectedDatetime, actual.getDatetime());
        }
    }

    @Nested
    @DisplayName("find nested unit tests")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFactory.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("find one entity by Id")
        void shouldReturnEntityByGivenId() {
            final var givenId = 1L;

            final var expectedId = 1L;
            final var expectedName = "stub name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFactory.NOW;

            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(entity -> {
                assertEquals(expectedId, entity.getId());
                assertEquals(expectedName, entity.getName());
                assertEquals(expectedCode, entity.getCode());
                assertEquals(expectedDatetime, entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("update nested tests")
    class UpdateTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFactory.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("update one entity by new values")
        void shouldUpdateDatabaseBySuccessfulUpdate() {
            final var givenId = 1L;
            final var givenNewName = "updated stub name";
            final var givenNewCode = 2;
            final var givenNewDatetime = StubFactory.TOMORROW;

            final var expectedName = "updated stub name";
            final var expectedCode = 2;
            final var expectedDatetime = StubFactory.TOMORROW;

            assertDoesNotThrow(() -> {
                var actual = systemUnderTest.findById(givenId);
                actual.ifPresent(entity -> {
                    entity.setName(givenNewName);
                    entity.setCode(givenNewCode);
                    entity.setDatetime(givenNewDatetime);
                });
            });

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(entity -> {
                assertEquals(expectedName, entity.getName());
                assertEquals(expectedCode, entity.getCode());
                assertEquals(expectedDatetime, entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("delete nested unit tests")
    class DeleteTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFactory.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("delete one entity by Id")
        void shouldDeleteTupleFromDatabaseByGivenId() {
            final var givenId = 1L;

            assertDoesNotThrow(() -> systemUnderTest.deleteById(givenId));
            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isEmpty());
        }

    }
}