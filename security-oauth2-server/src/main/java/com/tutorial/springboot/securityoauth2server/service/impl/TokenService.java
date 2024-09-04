package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.TokenDto;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentRoles;

@Service
public class TokenService {

    private final JwtEncoder jwtEncoder;

    private final JwtDecoder jwtDecoder;

    public TokenService(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public String extractUsername(String token) {
        return jwtDecoder.decode(token).getSubject();
    }

    public Instant extractExpiration(String token) {
        return jwtDecoder.decode(token).getExpiresAt();
    }

    public List<String> extractRoles(String token) {
        return jwtDecoder.decode(token).getClaim("roles");
    }

    public Optional<TokenDto> generateToken(String username) {
        var now = Instant.now();
        var expiration = now.plus(10, ChronoUnit.HOURS);

        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(username)
                .claim("roles", getCurrentRoles())
                .build();

        var token = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

        return Optional.of(new TokenDto(token, expiration));
    }

    public boolean isExpired(String token) {
        return Instant.now().isAfter(extractExpiration(token));
    }

    public boolean isValid(String token, String username) {
        return (extractUsername(token).equals(username) && !isExpired(token));
    }

}
