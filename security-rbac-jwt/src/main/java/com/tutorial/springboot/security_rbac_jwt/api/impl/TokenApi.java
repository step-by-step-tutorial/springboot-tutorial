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

@RequestMapping("/api/v1/token")
@RestController
public class TokenApi {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass().getSimpleName());

    private final TokenService tokenService;

    public TokenApi(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @GetMapping("/me/new")
    public ResponseEntity<TokenDto> generateToken() {
        logger.info("Received an inbound request to generate a token for user: {} through GET request", getCurrentUsername());
        return tokenService.generateToken(getCurrentUsername())
                .map(ResponseEntity::ok)
                .orElseThrow();
    }

    /**
     * Generates a JWT token to display on the home URL.
     * This method is the POST version of token generation, invoked via the {@code successForwardUrl}
     * during login process.
     *
     * @return the generated JWT token
     */
    @PostMapping("/me/new")
    public ResponseEntity<TokenDto> postGenerateToken() {
        logger.info("Received an inbound request to generate a token for user: {} based on login form.", getCurrentUsername());
        return tokenService.generateToken(getCurrentUsername())
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
