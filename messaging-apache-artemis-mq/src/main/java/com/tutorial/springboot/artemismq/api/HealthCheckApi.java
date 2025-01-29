package com.tutorial.springboot.artemismq.api;

import com.tutorial.springboot.artemismq.model.MessageModel;
import com.tutorial.springboot.artemismq.service.MainQueueService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/health-check")
public class HealthCheckApi {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(HealthCheckApi.class);

    private final MainQueueService mainQueueService;

    public HealthCheckApi(MainQueueService mainQueueService) {
        this.mainQueueService = mainQueueService;
    }

    @GetMapping
    public ResponseEntity<String> checkStatus() {
        logger.info("Application is up.");
        mainQueueService.push(new MessageModel(UUID.randomUUID().toString(), "Application is up."));
        return ResponseEntity.ok("UP");
    }
}
