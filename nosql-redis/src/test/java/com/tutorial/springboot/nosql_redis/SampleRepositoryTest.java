package com.tutorial.springboot.nosql_redis;

import com.tutorial.springboot.nosql_redis.model.SampleModel;
import com.tutorial.springboot.nosql_redis.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("jedis")
class SampleRepositoryTest {

    static final Logger LOGGER = LoggerFactory.getLogger(SampleRepositoryTest.class);

    @Autowired
    SampleRepository systemUnderTest;

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
    class SaveTest {

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
        }

        @Test
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
    class FindTest {

        String id = "";

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_MODEL).orElseThrow();
        }

        @Test
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
    class UpdateTest {
        String id = "";

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_MODEL).orElseThrow();
        }

        @Test
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
    class DeleteTest {
        String id = "";

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            this.id = systemUnderTest.save(StubFixturesFactory.SAMPLE_MODEL).orElseThrow();
        }

        @Test
        void shouldDeleteModelFromDatabaseByGivenId() {
            final var givenId = id;

            assertDoesNotThrow(() -> systemUnderTest.deleteById(givenId));
            final var actual = systemUnderTest.findById(givenId);

            assertTrue(actual.isEmpty());
        }
    }
}