package com.tutorial.springboot.securityoauth2client.api;

import com.tutorial.springboot.securityoauth2client.dto.OAuth2AuthenticationDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutorial.springboot.securityoauth2client.util.SecurityUtils.getCurrentOAuth2Authentication;

@RestController
public class ClientApi {

    @GetMapping("/")
    public OAuth2AuthenticationDto getIndex() {
        return getCurrentOAuth2Authentication();
    }
}
