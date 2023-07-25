package com.tutorial.springboot.rdbms_oracle;

import com.tutorial.springboot.rdbms_oracle.entity.SampleEntity;
import com.tutorial.springboot.rdbms_oracle.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
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
@DisplayName("unit tests of oracle sample repository")
@EnabledIfSystemProperty(named = "longTimeTest.isActivate", matches = "true")
class SampleRepositoryTest {

    @Container
    static final OracleContainer ORACLE = new OracleContainer("gvenzl/oracle-xe:21-slim-faststart");

    @Autowired
    SampleRepository systemUnderTest;

    static {
        ORACLE.withPassword("password");
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
    static void registerMySQLProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", ORACLE::getJdbcUrl);
        registry.add("spring.datasource.username", ORACLE::getUsername);
        registry.add("spring.datasource.password", ORACLE::getPassword);
        registry.add("spring.datasource.driver-class-name", ORACLE::getDriverClassName);
    }

    @BeforeAll
    static void beforeAll() {
        ORACLE.start();
    }

    @AfterAll
    static void afterAll() {
        ORACLE.stop();
    }

    @Nested
    @DisplayName("nested unit tests of save")
    class SaveTest {

        @Test
        @DisplayName("save an entity when there is not exception")
        void shouldReturnEntityWithIdBySuccessfulSave() {
            final var givenEntity = StubFixturesFactory.SAMPLE_ENTITY;

            final var expectedId = 1L;
            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

            final var actual = systemUnderTest.save(givenEntity);

            assertNotNull(actual);
            assertEquals(expectedId, actual.getId());
            assertEquals(expectedName, actual.getName());
            assertEquals(expectedCode, actual.getCode());
            assertEquals(expectedDatetime, actual.getDatetime());
        }
    }

    @Nested
    @DisplayName("nested unit tests of find")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFixturesFactory.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("find one entity by given Id")
        void shouldReturnEntityByGivenId() {
            final var givenId = 1L;

            final var expectedId = 1L;
            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

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
    @DisplayName("nested unit tests of update")
    class UpdateTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFixturesFactory.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("update one entity by given new values")
        void shouldUpdateTupleInDatabaseByGivenNewValues() {
            final var givenId = 1L;
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
                assertEquals(expectedName, entity.getName());
                assertEquals(expectedCode, entity.getCode());
                assertEquals(expectedDatetime, entity.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("nested unit tests of delete")
    class DeleteTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFixturesFactory.SAMPLE_ENTITY);
        }

        @Test
        @DisplayName("delete one entity by given Id")
        void shouldDeleteTupleFromDatabaseByGivenId() {
            final var givenId = 1L;

            assertDoesNotThrow(() -> systemUnderTest.deleteById(givenId));
            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isEmpty());
        }

    }
}
