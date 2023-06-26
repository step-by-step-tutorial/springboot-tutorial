package com.tutorial.springboot.messagingactivemq;

import com.tutorial.springboot.messagingactivemq.message.StringMessage;
import com.tutorial.springboot.messagingactivemq.service.StringMessagePublisher;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles({"test"})
class StringMessagePublisherTest {

    private static final EmbeddedActiveMQ activeMQ = new EmbeddedActiveMQ();

    @Autowired
    private StringMessagePublisher messagePublisher;

    @Autowired
    private MessagePublisherWatcher watcher;

    @BeforeAll
    static void startMessageQueue() throws Exception {
        Configuration config = new ConfigurationImpl();
        config.addAcceptorConfiguration("tcp", "tcp://localhost:61616");
        activeMQ.setConfiguration(config);
        activeMQ.start();
    }

    @AfterAll
    static void shutdownMessageQueue() throws Exception {
        activeMQ.stop();
    }

    @BeforeEach
    void setup() {
        watcher.reset();
    }

    @Test
    void givenNullMessage_thenShouldThrowNullPointerException() throws InterruptedException {
        StringMessage givenMessage = null;

        var actual = assertThrows(NullPointerException.class, () -> messagePublisher.publish(givenMessage));

        assertNotNull(actual);
        assertEquals(1, watcher.getTransactions().size());
        assertEquals(3, watcher.getTransactions().get(1).size());
        assertNull(watcher.getTransactions().get(1).get(0).value);
        assertEquals(NullPointerException.class, watcher.getTransactions().get(1).get(1).value.getClass());
        assertNull(watcher.getTransactions().get(1).get(2).value);
    }

    @Test
    void givenMessage_thenShouldSendMessage() {
        var givenId = UUID.randomUUID().toString();
        var givenText = "Hello world!, this message sent from localhost.";
        var givenMessage = new StringMessage(givenId, givenText);

        assertDoesNotThrow(() -> messagePublisher.publish(givenMessage));

        assertEquals(1, watcher.getTransactions().size());
        assertEquals(2, watcher.getTransactions().get(1).size());
        assertEquals(StringMessage.class, watcher.getTransactions().get(1).get(0).value.getClass());
        assertEquals(StringMessage.class, watcher.getTransactions().get(1).get(1).value.getClass());
    }

    @Test
    void givenTwoMessages_thenShouldSendMessage() {
        StringMessage[] givenMessages = givenMessages(2);

        assertDoesNotThrow(() -> messagePublisher.publish(givenMessages[0]));
        assertDoesNotThrow(() -> messagePublisher.publish(givenMessages[1]));

        assertEquals(2, watcher.getTransactions().size());
        assertEquals(2, watcher.getTransactions().get(1).size());
        assertEquals(2, watcher.getTransactions().get(2).size());
        assertEquals(StringMessage.class, watcher.getTransactions().get(1).get(0).value.getClass());
        assertEquals(StringMessage.class, watcher.getTransactions().get(1).get(1).value.getClass());
        assertEquals(StringMessage.class, watcher.getTransactions().get(2).get(0).value.getClass());
        assertEquals(StringMessage.class, watcher.getTransactions().get(2).get(1).value.getClass());
    }

    private static StringMessage[] givenMessages(int number) {
        StringMessage[] messages = new StringMessage[number];
        for (int i = 0; i < number; i++) {
            messages[i] = new StringMessage(UUID.randomUUID().toString(), String.format("message-%s", i));
        }
        return messages;
    }


}
