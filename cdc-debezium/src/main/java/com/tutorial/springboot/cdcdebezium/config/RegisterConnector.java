package com.tutorial.springboot.cdcdebezium.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

import static java.util.Objects.requireNonNull;

@Component
@Profile("debezium")
public class RegisterConnector {

    private final Logger logger = LoggerFactory.getLogger(RegisterConnector.class);

    private final WebClient webClient;

    private final ObjectMapper objectMapper;

    @Value("${debezium.register-path}")
    private String registerPath;

    @Value("${debezium.connectors-file}")
    String connectorsFilePath;

    public RegisterConnector(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    @PostConstruct
    private void register() {
        requireNonNull(connectorsFilePath, "connectors file path should not be null");
        var resource = new ClassPathResource(connectorsFilePath);
        requireNonNull(registerPath, "register path should not be null");
        requireNonNull(resource, "connectors file should not be null");

        try (var inputStream = resource.getInputStream();) {
            var connectors = objectMapper.readTree(inputStream);
            if (connectors.isArray()) {
                for (JsonNode connector : connectors) {
                    logger.info("Sent request to register a connector: {}.", connector.get("name").asText());
                    webClient.post()
                            .uri(registerPath)
                            .bodyValue(connector.toString())
                            .retrieve()
                            .bodyToMono(String.class)
                            .subscribe(s -> logger.info("The {} connector registered.", connector.get("name").asText()));
                }
            } else {
                logger.info("The connectors.json does not contain an array of connectors.");
            }
        } catch (IOException e) {
            logger.error("Loading connectors.json failed due to {}.", e.getMessage());
        }
    }

}
