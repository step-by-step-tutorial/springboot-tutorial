package com.tutorial.springboot.securityoauth2server.repository.token;

import com.tutorial.springboot.securityoauth2server.entity.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {
}