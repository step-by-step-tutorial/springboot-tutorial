package com.tutorial.springboot.securityoauth2client.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ClientApi {

    @GetMapping("/")
    public String getIndex() {
        return "Hello Client!";
    }
}
