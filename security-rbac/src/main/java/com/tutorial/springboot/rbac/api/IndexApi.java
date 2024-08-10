package com.tutorial.springboot.rbac.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexApi {

    @GetMapping("/")
    @PreAuthorize("isAuthenticated()")
    public String hello() {
        return "Login successful!";
    }
}
