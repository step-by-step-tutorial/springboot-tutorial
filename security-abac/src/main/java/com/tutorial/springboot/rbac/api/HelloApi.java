package com.tutorial.springboot.rbac.api;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloApi {

    @GetMapping("/admin/hello")
    @PreAuthorize("hasAuthority('ADMIN') and hasPermission(null, 'READ_PRIVILEGE')")
    public String adminHello() {
        return "Hello Admin";
    }

    @GetMapping("/user/hello")
    @PreAuthorize("hasAuthority('USER') and hasPermission(null, 'READ_PRIVILEGE')")
    public String userHello() {
        return "Hello User";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World";
    }
}
