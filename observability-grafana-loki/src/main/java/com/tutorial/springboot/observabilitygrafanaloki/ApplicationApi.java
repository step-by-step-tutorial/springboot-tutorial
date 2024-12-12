package com.tutorial.springboot.observabilitygrafanaloki;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
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

    private final Counter statusCounter;


    public ApplicationApi(MeterRegistry meterRegistry) {
        this.statusCounter = meterRegistry.counter("status_counter");
    }

    @GetMapping("/status")
    public ResponseEntity<String> checkStatus() {
        logger.info("Received an inbound request to test trace and Span of Jaeger");
        statusCounter.increment();

        return ResponseEntity.ok("UP");
    }
}
