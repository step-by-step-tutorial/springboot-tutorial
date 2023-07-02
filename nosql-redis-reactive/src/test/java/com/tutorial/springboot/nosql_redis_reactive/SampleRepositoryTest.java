package com.tutorial.springboot.nosql_redis_reactive;

import com.tutorial.springboot.nosql_redis_reactive.entity.SampleModel;
import com.tutorial.springboot.nosql_redis_reactive.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("unit tests of sample repository")
class SampleRepositoryTest {

    static final Logger logger = LoggerFactory.getLogger(SampleRepositoryTest.class);

    static RedisServer redisServer;

    static {
        try {
            redisServer = new RedisServer();
        } catch (IOException exception) {
            logger.error("construction of the Redis server failed due to: {}", exception.getMessage());
        }
    }

    @Autowired
    SampleRepository systemUnderTest;

    /**
     * This class includes Stub data.
     */
    static class StubFactory {
        static final LocalDateTime NOW = LocalDateTime.now();
        static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
        static final SampleModel SAMPLE_MODEL = SampleModel.create()
                .setName("stub name")
                .setCode(1)
                .setDatetime(NOW);

    }

    /**
     * This method executes final-all query then return all available ID as a  {@link String} array.
     *
     * @return all available ID as a  {@link String} array
     */
    private String[] givenId() {
        return systemUnderTest.findAll()
                .stream()
                .map(SampleModel::getId)
                .toArray(String[]::new);
    }

    @BeforeAll
    static void setUp() {
        redisServer.start();
    }

    @AfterAll
    static void tearDown() {
        try {
            redisServer.stop();
        }catch (Exception e){

        }
    }

    @Nested
    @DisplayName("save nested unit tests")
    class SaveTest {
        @Test
        @DisplayName("save an model")
        void shouldReturnIdBySuccessfulSave() {
            final var givenModel = StubFactory.SAMPLE_MODEL;

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
    @DisplayName("find nested unit tests")
    class FindTest {

        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFactory.SAMPLE_MODEL);
        }

        @Test
        @DisplayName("find one model by Key")
        void shouldReturnModelByGivenKey() {
            final var givenId = givenId()[0];

            final var expectedName = "stub name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFactory.NOW;

            final var actual = systemUnderTest.findByKey(givenId);

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
    @DisplayName("update nested unit tests")
    class UpdateTest {
        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFactory.SAMPLE_MODEL);
        }

        @Test
        @DisplayName("update one model by new values")
        void shouldUpdateDatabaseBySuccessfulUpdate() {
            final var givenId = givenId()[0];
            final var givenNewName = "updated stub name";
            final var givenNewCode = 2;
            final var givenNewDatetime = StubFactory.TOMORROW;

            final var expectedName = "updated stub name";
            final var expectedCode = 2;
            final var expectedDatetime = StubFactory.TOMORROW;

            assertDoesNotThrow(() -> {
                final var actual = systemUnderTest.findByKey(givenId).orElseThrow();
                actual.setName(givenNewName);
                actual.setCode(givenNewCode);
                actual.setDatetime(givenNewDatetime);

                systemUnderTest.update(actual);
            });

            final var actual = systemUnderTest.findByKey(givenId);

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
    @DisplayName("delete nested unit tests")
    class DeleteTest {
        @BeforeEach
        void initDatabase() {
            systemUnderTest.save(StubFactory.SAMPLE_MODEL);
        }


        @Test
        @DisplayName("delete one model by Id")
        void shouldDeleteModelFromDatabaseByGivenId() {
            final var givenId = givenId()[0];

            assertDoesNotThrow(() -> systemUnderTest.deleteById(givenId));
            final var actual = systemUnderTest.findByKey(givenId);

            assertTrue(actual.isEmpty());
        }
    }
}