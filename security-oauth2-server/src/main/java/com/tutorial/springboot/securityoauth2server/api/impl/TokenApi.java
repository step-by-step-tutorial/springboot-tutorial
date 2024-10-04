package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.dto.TokenDto;
import com.tutorial.springboot.securityoauth2server.service.impl.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;

@RestController
@RequestMapping("/api/v1/token")
public class TokenApi {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final TokenService tokenService;

    public TokenApi(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/new")
    public ResponseEntity<TokenDto> generateToken() {
        logger.info("Received an inbound request to generate a token for user:{}", getCurrentUsername());
        return tokenService.generateToken(getCurrentUsername())
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
