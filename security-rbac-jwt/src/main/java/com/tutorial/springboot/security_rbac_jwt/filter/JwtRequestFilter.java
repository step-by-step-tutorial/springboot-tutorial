package com.tutorial.springboot.security_rbac_jwt.filter;

import com.tutorial.springboot.security_rbac_jwt.service.impl.TokenService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.tutorial.springboot.security_rbac_jwt.util.HttpUtils.extractToken;
import static com.tutorial.springboot.security_rbac_jwt.util.SecurityUtils.setAuthenticationIfAbsent;
import static java.util.Objects.requireNonNull;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final TokenService tokenService;

    public JwtRequestFilter(UserDetailsService userDetailsService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        try {
            var token = extractToken(request);
            requireNonNull(token);
            var username = tokenService.extractUsername(token);
            requireNonNull(username);

            if (tokenService.isValid(token, username)) {
                var user = userDetailsService.loadUserByUsername(username);
                setAuthenticationIfAbsent(request, user);
            }

        } catch (NullPointerException e) {
            logger.warn("JWT token is invalid");
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token is expired");
        } catch (Exception e) {
            logger.warn("Unable to extract JWT token");
        }

        chain.doFilter(request, response);
    }

}
