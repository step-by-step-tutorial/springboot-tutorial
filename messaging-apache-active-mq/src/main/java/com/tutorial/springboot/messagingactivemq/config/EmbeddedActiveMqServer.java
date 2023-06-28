package com.tutorial.springboot.messagingactivemq.config;

import org.apache.activemq.artemis.core.config.impl.ConfigurationImpl;
import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"embedded"})
public class EmbeddedActiveMqServer implements DisposableBean {

    private final Logger logger = LoggerFactory.getLogger(EmbeddedActiveMqServer.class);

    private final EmbeddedActiveMQ activeMqServer = new EmbeddedActiveMQ();

    public EmbeddedActiveMqServer(
            @Value("${spring.artemis.host}")
            final String host,
            @Value("${spring.artemis.port}")
            final String port) {
        try {
            org.apache.activemq.artemis.core.config.Configuration config = new ConfigurationImpl();
            config.addAcceptorConfiguration("tcp", String.format("tcp://%s:%s", host, port));
            activeMqServer.setConfiguration(config);
            activeMqServer.start();
            logger.info("embedded active-mq has started");
        } catch (Exception e) {
            logger.error("embedded active-mq failed due to: {}", e.getMessage());
        }
    }

    @Override
    public void destroy() throws Exception {
            logger.info("embedded active-mq has stopped");
        activeMqServer.stop();
    }
}