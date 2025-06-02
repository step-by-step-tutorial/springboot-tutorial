package com.tutorial.springboot.nosql_redis_reactive;

import com.tutorial.springboot.nosql_redis_reactive.entity.SampleModel;
import com.tutorial.springboot.nosql_redis_reactive.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("unit tests of reactive redis sample repository")
class SampleRepositoryTest {

    @Autowired
    SampleRepository systemUnderTest;

    /**
     * This class includes Stub data.
     */
    static class StubFixturesFactory {
        static final LocalDateTime NOW = LocalDateTime.now();
        static final LocalDateTime TOMORROW = LocalDateTime.now().plusDays(1);
        static final SampleModel SAMPLE_MODEL = SampleModel.create()
                .setName("name")
                .setCode(1)
                .setDatetime(NOW);
    }

    @Nested
    @DisplayName("nested unit tests of save")
    class SaveTest {
        @Test
        @DisplayName("save an model when there is no exception")
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
                final var currentStateOfModel = systemUnderTest.findByKey(givenId).orElseThrow();
                currentStateOfModel.setName(givenNewName);
                currentStateOfModel.setCode(givenNewCode);
                currentStateOfModel.setDatetime(givenNewDatetime);

                systemUnderTest.update(currentStateOfModel);
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
            final var actual = systemUnderTest.findByKey(givenId);

            assertTrue(actual.isEmpty());
        }
    }
}
