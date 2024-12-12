package com.tutorial.springboot.monitorjaeger.api;

import io.opentelemetry.api.trace.Span;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/application")
public class ApplicationApi {

    private final Logger logger = LoggerFactory.getLogger(ApplicationApi.class);

    @GetMapping("/status")
    public ResponseEntity<String> checkStatus() {
        logger.info("Received an inbound request to test trace and Span of Jaeger");

        var span = Span.current();
        if (span != null) {
            span.addEvent("Test span: Test is successful");
        }

        return ResponseEntity.ok("UP");
    }
}
