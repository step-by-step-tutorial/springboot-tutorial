package com.tutorial.springboot.messaging_artemis_mq.config;

import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"embedded-artemis"})
public class EmbeddedActiveMqServer implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedActiveMqServer.class);

    private final EmbeddedActiveMQ activeMqServer = new EmbeddedActiveMQ();

    public EmbeddedActiveMqServer(@Value("${spring.artemis.broker-url}") final String brokerUrl) {
        try {
            var config = new ConfigurationImpl();
            config.addAcceptorConfiguration("embeddedartemis", String.format("tcp://%s", brokerUrl));
            activeMqServer.setConfiguration(config);
            activeMqServer.start();
            logger.info("embedded active-mq-artemis has started");
        } catch (Exception e) {
            logger.error("embedded active-mq-artemis failed due to: {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
        logger.info("embedded active-mq-artemis has stopped");
        activeMqServer.stop();
    }
}
