package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.CredentialsDto;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    public void authenticate(CredentialsDto dto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.username(), dto.password()));
    }
}