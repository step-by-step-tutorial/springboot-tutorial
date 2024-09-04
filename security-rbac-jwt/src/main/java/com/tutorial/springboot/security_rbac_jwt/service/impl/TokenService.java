package com.tutorial.springboot.security_rbac_jwt.service.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.TokenDto;
import com.tutorial.springboot.security_rbac_jwt.util.KeyUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.tutorial.springboot.security_rbac_jwt.util.DateUtils.currentDate;
import static com.tutorial.springboot.security_rbac_jwt.util.DateUtils.getFutureDateIn;
import static com.tutorial.springboot.security_rbac_jwt.util.SecurityUtils.getCurrentRoles;

@Component
public class TokenService {

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(KeyUtils.SIGN_IN_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @SuppressWarnings("unchecked")
    public <T> T extractClaimByName(String token, String name) {
        return (T) extractAllClaims(token).get(name);
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public Date extractExpirationData(String token) {
        return extractAllClaims(token).getExpiration();
    }

    public List<String> extractRoles(String token) {
        return extractClaimByName(token, "roles");
    }

    public Optional<TokenDto> generateToken(String username) {
        var expirationDate = getFutureDateIn(10);
        var token = Jwts.builder()
                .claim("roles", getCurrentRoles())
                .subject(username)
                .issuedAt(currentDate())
                .expiration(expirationDate)
                .signWith(KeyUtils.SIGN_IN_KEY)
                .compact();

        return Optional.of(new TokenDto(token, expirationDate));
    }

    private Boolean isExpired(String token) {
        return currentDate().after(extractExpirationData(token));
    }

    public Boolean isValid(String token, String username) {
        return (extractUsername(token).equals(username) && !isExpired(token));
    }

}
