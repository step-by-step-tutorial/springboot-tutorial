package com.tutorial.springboot.securityoauth2client.api.impl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthApi {

    @GetMapping
    public ResponseEntity<String> getHealthCheck() {
        return ResponseEntity.ok("UP");
    }

    @PostMapping
    public ResponseEntity<String> postHealthCheck() {
        return ResponseEntity.ok("UP");
    }
}
