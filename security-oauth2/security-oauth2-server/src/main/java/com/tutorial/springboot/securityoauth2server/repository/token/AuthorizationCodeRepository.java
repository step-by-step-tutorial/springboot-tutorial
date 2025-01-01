package com.tutorial.springboot.securityoauth2server.repository.token;

import com.tutorial.springboot.securityoauth2server.entity.AuthorizationCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorizationCodeRepository extends JpaRepository<AuthorizationCode, Long> {
}