package com.tutorial.springboot.security_authentication_inmemory;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApiController {

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }
}
