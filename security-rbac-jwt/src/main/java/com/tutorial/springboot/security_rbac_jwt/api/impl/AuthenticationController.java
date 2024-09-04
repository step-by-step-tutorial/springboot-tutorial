package com.tutorial.springboot.security_rbac_jwt.api.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.TokenDto;
import com.tutorial.springboot.security_rbac_jwt.service.impl.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutorial.springboot.security_rbac_jwt.util.SecurityUtils.getCurrentUsername;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthenticationController {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final TokenService tokenService;

    public AuthenticationController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping("/token")
    public ResponseEntity<TokenDto> authenticate() {
        logger.info("Received an inbound request to generate a token for user:{}", getCurrentUsername());
        return tokenService.generateToken(getCurrentUsername())
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
