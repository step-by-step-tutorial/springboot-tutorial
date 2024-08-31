package com.tutorial.springboot.security_rbac_jwt.service;

import com.tutorial.springboot.security_rbac_jwt.dto.TokenDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Function;

import static com.tutorial.springboot.security_rbac_jwt.util.KeyUtils.generateSecretKey;
import static com.tutorial.springboot.security_rbac_jwt.util.KeyUtils.getSignInKey;

@Component
public class JwtService {

    private final String secretKey = generateSecretKey();

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignInKey(secretKey))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public Optional<TokenDto> generateToken(String username) {
        var token = Jwts.builder()
                .claims(new HashMap<>())
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(getSignInKey(secretKey))
                .compact();

        return Optional.of(new TokenDto(token, extractExpiration(token)));
    }

    private Boolean isExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Boolean isValid(String token, String username) {
        return (extractUsername(token).equals(username) && !isExpired(token));
    }

}
