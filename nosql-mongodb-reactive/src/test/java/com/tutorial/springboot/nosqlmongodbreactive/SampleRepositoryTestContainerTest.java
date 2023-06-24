package com.tutorial.springboot.nosqlmongodbreactive;

import com.tutorial.springboot.nosqlmongodbreactive.document.SampleDocument;
import com.tutorial.springboot.nosqlmongodbreactive.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@DataMongoTest
@ActiveProfiles("test")
@DisplayName("nosql-mongodb-reactive: {@link SampleRepository} unit tests")
class SampleRepositoryTestContainerTest {

    @Container
    static final MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:5.0.16");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDbContainer::getReplicaSetUrl);
    }

    @BeforeAll
    static void setUp() {
        mongoDbContainer.start();
    }

    @AfterAll
    static void tearDown() {
        mongoDbContainer.stop();
    }

    @Autowired
    SampleRepository underTest;

    /**
     * This class includes Stubs data.
     */
    static class Stub {
        static SampleDocument SAMPLE_DOCUMENT = SampleDocument.create()
                .setName("test")
                .setCode(1)
                .setDatetime(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
    }

    /**
     * This method executes final-all query then return the ID of first tuple, otherwise return {@value "invalid"} as a
     * {@link String} value.
     *
     * @return ID of first tuple, otherwise return {@value "invalid"} as a {@link String} value
     */
    String giveMeId() {
        var documents = underTest.findAll()
                .collectList()
                .blockOptional();

        assertNotNull(documents);
        assertTrue(documents.isPresent());

        return documents.get()
                .stream()
                .map(SampleDocument::getId)
                .findFirst()
                .orElse("invalid");
    }

    @Nested
    @DisplayName("save nested tests")
    class SaveTest {

        @BeforeEach
        void preCondition() {
            assertNotNull(underTest);
        }

        @Test
        @DisplayName("save a document")
        void GivenDocument_WhenInvokeSaveMethod_ThenReturnsPersistedDocument() {
            var givenDocument = Stub.SAMPLE_DOCUMENT;

            var actual = underTest.save(givenDocument).blockOptional();

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(document -> assertNotNull(document.getId()));
        }
    }

    @Nested
    @DisplayName("find nested tests")
    class FindTest {

        @BeforeEach
        void prepareData() {
            assertNotNull(underTest);
            underTest.save(Stub.SAMPLE_DOCUMENT).block();
        }

        @Test
        @DisplayName("find one document by ID")
        void GivenId_WhenInvokeFindByIdMethod_ThenReturnsPersistedDocument() {
            var givenId = giveMeId();

            var expectedDocument = Stub.SAMPLE_DOCUMENT;

            var actual = underTest.findById(givenId).blockOptional();

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(document -> {
                assertEquals(expectedDocument.getName(), document.getName());
                assertEquals(expectedDocument.getCode(), document.getCode());
                assertEquals(expectedDocument.getDatetime(), document.getDatetime());
            });
        }
    }
}
