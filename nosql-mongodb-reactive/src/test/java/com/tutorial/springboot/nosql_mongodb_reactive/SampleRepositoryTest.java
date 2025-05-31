package com.tutorial.springboot.nosql_mongodb_reactive;

import com.tutorial.springboot.nosql_mongodb_reactive.document.SampleDocument;
import com.tutorial.springboot.nosql_mongodb_reactive.repository.SampleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@ActiveProfiles("test")
class SampleRepositoryTest {

    @Autowired
    SampleRepository systemUnderTest;

    /**
     * This class includes Stubs data.
     */
    static class StubFixturesFactory {
        static final LocalDateTime NOW = LocalDateTime.now()
                .truncatedTo(ChronoUnit.SECONDS);
        static final LocalDateTime TOMORROW = LocalDateTime.now()
                .plusDays(1)
                .truncatedTo(ChronoUnit.SECONDS);
        static final SampleDocument SAMPLE_DOCUMENT = SampleDocument.create()
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
        void shouldReturnDocumentWithIdBySuccessfulSave() {
            final var givenDocument = StubFixturesFactory.SAMPLE_DOCUMENT;

            final var actual = systemUnderTest.save(givenDocument)
                    .blockOptional();

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(document -> assertNotNull(document.getId()));
        }
    }

    @Nested
    class FindTest {

        String id;

        @BeforeEach
        void setUp() {
            assertNotNull(systemUnderTest);
            systemUnderTest.save(StubFixturesFactory.SAMPLE_DOCUMENT)
                    .blockOptional()
                    .ifPresent(document -> this.id = document.getId());
        }

        @Test
        void shouldReturnModelByGivenId() {
            final var givenId = id;

            final var expectedName = "name";
            final var expectedCode = 1;
            final var expectedDatetime = StubFixturesFactory.NOW;

            final var actual = systemUnderTest.findById(givenId)
                    .blockOptional();

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(document -> {
                assertEquals(expectedName, document.getName());
                assertEquals(expectedCode, document.getCode());
                assertEquals(expectedDatetime, document.getDatetime());
            });
        }
    }
}
