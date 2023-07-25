package com.tutorial.springboot.nosql_redis;

import com.tutorial.springboot.nosql_redis.model.SampleModel;
import com.tutorial.springboot.nosql_redis.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("jedis")
@DisplayName("unit tests of redis sample repository")
class SampleRepositoryTest {

    static final Logger LOGGER = LoggerFactory.getLogger(SampleRepositoryTest.class);

    static RedisServer redisServer;

    @Autowired
    SampleRepository systemUnderTest;

    static {
        try {
            redisServer = new RedisServer(6379);
        } catch (IOException exception) {
            LOGGER.error("construction of the Redis server failed due to: {}", exception.getMessage());
        }
    }

    @BeforeAll
    static void setUp() {
        redisServer.start();
    }

    @AfterAll
    static void tearDown() {
        try {
            redisServer.stop();
        } catch (Exception exception) {
            LOGGER.error("stopping embedded Redis server failed due to: {}", exception.getMessage());
        }
    }
    /**
     * This class includes Stub data.
     */
    static class StubFixturesFactory {
        static final LocalDateTime NOW = LocalDateTime.now();
        static final LocalDateTime TOMORROW = LocalDateTime.now()
                .plusDays(1);
        static final SampleModel SAMPLE_MODEL = SampleModel.create()
                .setName("name")
                .setCode(1)
                .setDatetime(NOW);
    }


    @Nested
    @DisplayName("nested unit tests of save")
    class SaveTest {

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
        }

        @Test
        @DisplayName("save a model when there is no exception")
        void shouldReturnIdBySuccessfulSave() {
            final var givenModel = StubFixturesFactory.SAMPLE_MODEL;

            final var actual = systemUnderTest.save(givenModel);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                final var uuid = UUID.fromString(id);
                assertNotNull(uuid);
            });
        }
    }

    @Nested
    @DisplayName("nested unit tests of find")
    class FindTest {

        String id = "";

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_MODEL).orElseThrow();
        }

        @Test
        @DisplayName("find one model by given Key")
        void shouldReturnModelByGivenKey() {
            final var givenId = id;

            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

            final var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(model -> {
                assertEquals(expectedName, model.getName());
                assertEquals(expectedCode, model.getCode());
                assertEquals(expectedDatetime, model.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("nested unit tests of update")
    class UpdateTest {
        String id = "";

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_MODEL).orElseThrow();
        }

        @Test
        @DisplayName("update one model by given new values")
        void shouldUpdateModelInDatabaseByGivenNewValues() {
            final var givenId = id;
            final var givenNewName = "updated name";
            final var givenNewCode = 2;
            final var givenNewDatetime = StubFixturesFactory.TOMORROW;

            final var expectedName = "updated name";
            final var expectedCode = 2;
            final var expectedDatetime = StubFixturesFactory.TOMORROW;

            assertDoesNotThrow(() -> {
                final var currentStateOfModel = systemUnderTest.findById(givenId).orElseThrow();
                currentStateOfModel.setName(givenNewName);
                currentStateOfModel.setCode(givenNewCode);
                currentStateOfModel.setDatetime(givenNewDatetime);

                systemUnderTest.update(currentStateOfModel);
            });

            final var actual = systemUnderTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(model -> {
                assertEquals(expectedName, model.getName());
                assertEquals(expectedCode, model.getCode());
                assertEquals(expectedDatetime, model.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("nested unit tests of delete")
    class DeleteTest {
        String id = "";

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_MODEL).orElseThrow();
        }
        @Test
        @DisplayName("delete one model by given Id")
        void shouldDeleteModelFromDatabaseByGivenId() {
            final var givenId = id;

            assertDoesNotThrow(() -> systemUnderTest.deleteById(givenId));
            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isEmpty());
        }
    }
}