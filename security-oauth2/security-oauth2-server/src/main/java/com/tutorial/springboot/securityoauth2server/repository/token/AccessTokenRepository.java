package com.tutorial.springboot.securityoauth2server.repository.token;

import com.tutorial.springboot.securityoauth2server.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
}