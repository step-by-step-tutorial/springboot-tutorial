package com.tutorial.springboot.securityoauth2client.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientRest {

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }
}
