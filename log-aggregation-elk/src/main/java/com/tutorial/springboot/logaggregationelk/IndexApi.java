package com.tutorial.springboot.logaggregationelk;

import org.slf4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexApi {

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(IndexApi.class);

    @GetMapping("/")
    public String index() {
        logger.info("Hello World!");
        return "Hello World!";
    }
}
