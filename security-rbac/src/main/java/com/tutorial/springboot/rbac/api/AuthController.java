package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.LoginResponse;
import com.tutorial.springboot.rbac.dto.LoginUserDto;
import com.tutorial.springboot.rbac.service.AuthenticationService;
import com.tutorial.springboot.rbac.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final JwtService jwtService;

    private final AuthenticationService authenticationService;

    public AuthController(JwtService jwtService, AuthenticationService authenticationService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto dto) {
        authenticationService.authenticate(dto);
        var jwtToken = jwtService.generateToken(dto.username());
        return ok(new LoginResponse(jwtToken, jwtService.extractExpiration(jwtToken)));
    }
}
