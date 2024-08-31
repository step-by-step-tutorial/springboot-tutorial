package com.tutorial.springboot.security_rbac_jwt.api.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.CredentialsDto;
import com.tutorial.springboot.security_rbac_jwt.dto.TokenDto;
import com.tutorial.springboot.security_rbac_jwt.service.AuthenticationService;
import com.tutorial.springboot.security_rbac_jwt.service.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenDto> authenticate(@RequestBody CredentialsDto dto) {
        logger.info("Received an inbound request to generate a token for user:{}", dto.username());
        authenticationService.authenticate(dto);
        return jwtService.generateToken(dto.username())
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
