package com.tutorial.springboot.securityoauth2server.repository.token;

import com.tutorial.springboot.securityoauth2server.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
}