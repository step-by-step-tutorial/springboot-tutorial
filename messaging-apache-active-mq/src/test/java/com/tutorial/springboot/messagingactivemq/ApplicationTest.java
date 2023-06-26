package com.tutorial.springboot.messagingactivemq;

import com.tutorial.springboot.messagingactivemq.message.StringMessage;
import com.tutorial.springboot.messagingactivemq.service.StringMessagePublisher;
import org.apache.activemq.artemis.core.config.Configuration;
import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@ActiveProfiles({"test"})
class ApplicationTest {

    private final EmbeddedActiveMQ activeMQ = new EmbeddedActiveMQ();

    @Autowired
    private StringMessagePublisher messagePublisher;

    @BeforeEach
    void setUp() throws Exception {
        Configuration config = new ConfigurationImpl();
        config.addAcceptorConfiguration("tcp", "tcp://localhost:61616");
        activeMQ.setConfiguration(config);
        activeMQ.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        activeMQ.stop();
    }

    @Test
    void shouldSendMessage() throws InterruptedException {
        var givenId = UUID.randomUUID().toString();
        var givenText = "Hello world!, this message sent from localhost.";
        var givenMessage = new StringMessage(givenId, givenText);

        assertDoesNotThrow(() -> messagePublisher.sendMessage(givenMessage));

        Thread.sleep(1000);
    }


}
