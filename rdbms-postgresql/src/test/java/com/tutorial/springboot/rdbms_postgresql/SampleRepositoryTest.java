package com.tutorial.springboot.rdbms_postgresql;

import com.tutorial.springboot.rdbms_postgresql.entity.SampleEntity;
import com.tutorial.springboot.rdbms_postgresql.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles({"test"})
@DisplayName("unit tests of postgresql sample repository")
class SampleRepositoryTest {
    static final Logger LOGGER = LoggerFactory.getLogger(SampleRepositoryTest.class.getSimpleName());

    @Container
    static final PostgreSQLContainer<?> POSTGRESQL = new PostgreSQLContainer<>("postgres:13.9-alpine");

    @Autowired
    SampleRepository systemUnderTest;


    static {
        try {
            POSTGRESQL.withDatabaseName("test_db")
                    .withUsername("user")
                    .withPassword("password");
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }

    /**
     * This class includes STUB data.
     */
    static class StubFixturesFactory {
        static final LocalDateTime NOW = LocalDateTime.now();
        static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
        static final SampleEntity SAMPLE_ENTITY = SampleEntity.create()
                .setName("name")
                .setCode(1)
                .setDatetime(NOW);
    }

    @SuppressWarnings({"unused"})
    @DynamicPropertySource
    static void registerPostgreSQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRESQL::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRESQL::getUsername);
        registry.add("spring.datasource.password", POSTGRESQL::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRESQL::getDriverClassName);
    }

    @BeforeAll
    static void beforeAll() {
        POSTGRESQL.start();
    }

    @AfterAll
    static void afterAll() {
        POSTGRESQL.stop();
    }

    @Nested
    @DisplayName("nested unit tests of save")
    class SaveTest {

        @Test
        @DisplayName("save an entity when there is not exception")
        void shouldReturnEntityWithIdBySuccessfulSave() {
            final var givenEntity = StubFixturesFactory.SAMPLE_ENTITY;

            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

            final var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertNotNull(actual.getId());
            assertEquals(expectedName, actual.getName());
            assertEquals(expectedCode, actual.getCode());
            assertEquals(expectedDatetime, actual.getDatetime());
        }
    }

    @Nested
    @DisplayName("nested unit tests of find")
    class FindTest {
        private Long id;

        @BeforeEach
        void initDatabase() {
            final var entity = systemUnderTest.save(StubFixturesFactory.SAMPLE_ENTITY);
            id = entity.getId();
        }

        @Test
        @DisplayName("find one entity by given Id")
        void shouldReturnEntityByGivenId() {
            final var givenId = id;

            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(entity -> {
                assertEquals(expectedName, entity.getName());
                assertEquals(expectedCode, entity.getCode());
                assertEquals(expectedDatetime, entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("nested unit tests of update")
    class UpdateTest {

        private Long id;

        @BeforeEach
        void initDatabase() {
            final var entity = systemUnderTest.save(StubFixturesFactory.SAMPLE_ENTITY);
            id = entity.getId();
        }

        @Test
        @DisplayName("update one entity by given new values")
        void shouldUpdateTupleInDatabaseByGivenNewValues() {
            final var givenId = id;
            final var givenNewName = "updated name";
            final var givenNewCode = 2;
            final var givenNewDatetime = StubFixturesFactory.TOMORROW;

            final var expectedName = "updated name";
            final var expectedCode = 2;
            final var expectedDatetime = StubFixturesFactory.TOMORROW;

            assertDoesNotThrow(() -> {
                var currentStateOfEntity = systemUnderTest.findById(givenId);
                currentStateOfEntity.ifPresent(entity -> {
                    entity.setName(givenNewName);
                    entity.setCode(givenNewCode);
                    entity.setDatetime(givenNewDatetime);
                });
            });

            var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isPresent());
            actual.ifPresent(entity -> {
                assertNotNull(entity.getId());
                assertEquals(expectedName, entity.getName());
                assertEquals(expectedCode, entity.getCode());
                assertEquals(expectedDatetime, entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("nested unit tests of delete")
    class DeleteTest {
        private Long id;

        @BeforeEach
        void initDatabase() {
            final var entity = systemUnderTest.save(StubFixturesFactory.SAMPLE_ENTITY);
            id = entity.getId();
        }

        @Test
        @DisplayName("delete one entity by given Id")
        void shouldDeleteTupleFromDatabaseByGivenId() {
            final var givenId = id;

            assertDoesNotThrow(() -> systemUnderTest.deleteById(givenId));
            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isEmpty());
        }

    }
}
