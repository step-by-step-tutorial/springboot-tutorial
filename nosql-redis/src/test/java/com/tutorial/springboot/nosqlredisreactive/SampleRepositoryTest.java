package com.tutorial.springboot.nosqlredisreactive;

import com.tutorial.springboot.nosqlredisreactive.domain.SampleModel;
import com.tutorial.springboot.nosqlredisreactive.repository.SampleRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import redis.clients.jedis.JedisPool;
import redis.embedded.RedisServer;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("jedis")
@DisplayName("Sample Repository Tests")
class SampleRepositoryTest {

    private static RedisServer redisServer;

    private static JedisPool jedisPool;

    private static final Logger logger = LoggerFactory.getLogger(SampleRepositoryTest.class);

    @Autowired
    private SampleRepository underTest;

    static class Stub {
        static SampleModel SAMPLE = SampleModel.create()
                .setName("test")
                .setCode(1)
                .setDatetime(LocalDateTime.now());
    }

    @BeforeAll
    public static void setUp() {
        try {
            redisServer = new RedisServer();
            redisServer.start();
            jedisPool = new JedisPool();
        } catch (IOException exception) {
            logger.error(exception.getMessage());
        }
    }

    @AfterAll
    public static void tearDown() {
        try {
            jedisPool.close();
            redisServer.stop();
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }
    }

    private String givenId(SampleModel givenModel) {
        var id = underTest.save(givenModel);
        if (id.isPresent()) {
            var uuid = UUID.fromString(id.get());
            assertNotNull(uuid);
            return uuid.toString();
        } else {
            fail();
            return "invalid";
        }
    }

    @Nested
    @DisplayName("save nested tests")
    class SaveTest {
        @Test
        @DisplayName("save an model")
        void givenModel_whenInvokeSave_thenReturnsPersistedModel() {
            var givenModel = Stub.SAMPLE;

            var result = underTest.save(givenModel);

            assertNotNull(result);
            assertTrue(result.isPresent());
            result.ifPresent(id -> {
                var uuid = UUID.fromString(id);
                assertNotNull(uuid);
            });
        }
    }

    @Nested
    @DisplayName("find nested tests")
    class FindTest {

        @Test
        @DisplayName("find one model by ID")
        void givenId_whenInvokeFindById_thenReturnsPersistedModel() {
            var givenModel = Stub.SAMPLE;
            var givenId = givenId(givenModel);

            var result = underTest.findById(givenId);

            assertNotNull(result);
            assertTrue(result.isPresent());
            result.ifPresent(model -> {
                assertEquals(givenModel.getName(), model.getName());
                assertEquals(givenModel.getCode(), model.getCode());
                assertEquals(givenModel.getDatetime(), model.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("update nested tests")
    class UpdateTest {

        @Test
        @DisplayName("update one model by new values")
        void givenModel_whenInvokeUpdate_thenTupleWillBeUpdated() {
            var givenModel = Stub.SAMPLE;
            var givenId = givenId(givenModel);

            var beforeUpdate = underTest.findById(givenId);
            assertNotNull(beforeUpdate);
            assertTrue(beforeUpdate.isPresent());
            beforeUpdate.ifPresent(model -> {
                assertEquals(givenModel.getName(), model.getName());
                assertEquals(givenModel.getCode(), model.getCode());
                assertEquals(givenModel.getDatetime(), model.getDatetime());
            });

            givenModel.setName("updated_test");
            givenModel.setCode(2);
            underTest.update(givenModel);

            var afterUpdate = underTest.findById(givenId);
            assertNotNull(afterUpdate);
            assertTrue(afterUpdate.isPresent());
            afterUpdate.ifPresent(model -> {
                assertEquals("updated_test", model.getName());
                assertEquals(2, model.getCode());
                assertEquals(givenModel.getDatetime(), model.getDatetime());
            });
        }
    }

    @Nested
    @DisplayName("delete nested tests")
    class DeleteTest {
        @Test
        @DisplayName("delete one model by ID")
        void givenId_whenInvokeDeleteById_thenTupleWillBeDeletedFromDatabase() {

            var givenId = givenId(Stub.SAMPLE);

            underTest.deleteById(givenId);

            var afterDelete = underTest.findById(givenId);
            assertTrue(afterDelete.isEmpty());
        }
    }

}
