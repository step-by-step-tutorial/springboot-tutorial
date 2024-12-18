package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.dto.ListDto;
import com.tutorial.springboot.securityoauth2server.dto.StatusDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class ApplicationApi {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    @GetMapping(value = "/health")
    public ResponseEntity<StatusDto> getHealthCheck() {
        logger.info("Received an inbound request to check health status");
        return ResponseEntity.ok(new StatusDto("UP"));
    }

    @GetMapping(value = "/services")
    public ResponseEntity<ListDto> getServices() {
        logger.info("Received an inbound request to show all available services");
        return ResponseEntity.ok(new ListDto(List.of("User API", "Client API", "Token API")));
    }
}