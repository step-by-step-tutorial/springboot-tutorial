package com.tutorial.springboot.newsqlcockroachdb.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health-check")
public class HealthCheckApi {

    private final Logger logger = LoggerFactory.getLogger(HealthCheckApi.class);

    @GetMapping
    public ResponseEntity<String> checkStatus() {
        logger.info("Application is up.");
        return ResponseEntity.ok("UP");
    }
} 