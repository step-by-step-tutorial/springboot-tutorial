package com.tutorial.springboot.nosqlredis;

import com.tutorial.springboot.nosqlredis.model.SampleModel;
import com.tutorial.springboot.nosqlredis.repository.SampleRepository;
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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("jedis")
@DisplayName("nosql-redis: {@link SampleRepository} unit tests")
class SampleRepositoryTest {

    static final Logger LOGGER = LoggerFactory.getLogger(SampleRepositoryTest.class);

    static RedisServer redisServer;

    @Autowired
    SampleRepository underTest;

    /**
     * This class includes Stubs data.
     */
    static class Stub {
        static SampleModel SAMPLE_MODEL = SampleModel.create()
                .setName("test")
                .setCode(1)
                .setDatetime(LocalDateTime.now());
    }

    /**
     * This method executes final-all query then return the ID of first tuple, otherwise return {@value "invalid"} as a
     * {@link String} value.
     *
     * @return ID of first tuple, otherwise return {@value "invalid"} as a {@link String} value
     */
    private String givenId() {
        return underTest.findAll()
                .stream()
                .map(SampleModel::getId)
                .findFirst()
                .orElse("invalid");
    }

    @BeforeAll
    public static void initRedisServer() {
        try {
            redisServer = new RedisServer();
            redisServer.start();
        } catch (IOException exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    @AfterAll
    public static void ShutdownRedisServer() {
        try {
            redisServer.stop();
        } catch (Exception exception) {
            LOGGER.error(exception.getMessage());
        }
    }

    @Nested
    @DisplayName("save nested tests")
    class SaveTest {
        @Test
        @DisplayName("save a model")
        void GivenModel_WhenInvokeSaveMethod_ThenReturnsPersistedModel() {
            var givenModel = Stub.SAMPLE_MODEL;

            var actual = underTest.save(givenModel);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(id -> {
                var uuid = UUID.fromString(id);
                assertNotNull(uuid);
            });
        }
    }

    @Nested
    @DisplayName("find nested tests")
    class FindTest {

        @BeforeEach
        void prepareData() {
            SaveTest saveTest = new SaveTest();
            saveTest.GivenModel_WhenInvokeSaveMethod_ThenReturnsPersistedModel();
        }

        @Test
        @DisplayName("find one model by ID")
        void GivenId_WhenInvokeFindByIdMethod_ThenReturnsPersistedModel() {
            var givenId = givenId();

            var expectedModel = Stub.SAMPLE_MODEL;

            var actual = underTest.findById(givenId);

            assertNotNull(actual);
            assertTrue(actual.isPresent());
            actual.ifPresent(model -> {
                Assertions.assertEquals(expectedModel.getName(), model.getName());
                Assertions.assertEquals(expectedModel.getCode(), model.getCode());
                Assertions.assertEquals(expectedModel.getDatetime(), model.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("update nested tests")
    class UpdateTest {
        @BeforeEach
        void prepareData() {
            SaveTest saveTest = new SaveTest();
            saveTest.GivenModel_WhenInvokeSaveMethod_ThenReturnsPersistedModel();
        }

        @Test
        @DisplayName("update one model by new values")
        void GivenModel_WhenInvokeUpdateMethod_ThenTupleWillBeUpdated() {
            var givenModel = Stub.SAMPLE_MODEL;
            var givenId = givenId();

            var beforeUpdate = underTest.findById(givenId);
            assertNotNull(beforeUpdate);
            assertTrue(beforeUpdate.isPresent());
            beforeUpdate.ifPresent(model -> {
                Assertions.assertEquals("test", model.getName());
                Assertions.assertEquals(1, model.getCode());
                Assertions.assertEquals(givenModel.getDatetime(), model.getDatetime());
            });

            givenModel.setName("updated_test");
            givenModel.setCode(2);
            underTest.update(givenModel);

            var afterUpdate = underTest.findById(givenId);

            assertNotNull(afterUpdate);
            assertTrue(afterUpdate.isPresent());
            afterUpdate.ifPresent(model -> {
                Assertions.assertEquals("updated_test", model.getName());
                Assertions.assertEquals(2, model.getCode());
                Assertions.assertEquals(givenModel.getDatetime(), model.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("delete nested tests")
    class DeleteTest {

        @BeforeEach
        void prepareData() {
            SaveTest saveTest = new SaveTest();
            saveTest.GivenModel_WhenInvokeSaveMethod_ThenReturnsPersistedModel();
        }

        @Test
        @DisplayName("delete one model by ID")
        void GivenId_WhenInvokeDeleteByIdMethod_ThenTupleWillBeDeletedFromDatabase() {

            var givenId = givenId();

            underTest.deleteById(givenId);

            var afterDelete = underTest.findById(givenId);
            assertTrue(afterDelete.isEmpty());
        }
    }

}
