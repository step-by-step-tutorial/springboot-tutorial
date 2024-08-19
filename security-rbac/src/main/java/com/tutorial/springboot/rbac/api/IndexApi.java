package com.tutorial.springboot.rbac.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexApi {

    @GetMapping("/")
    public String home() {
        return "Login successful!";
    }
}
