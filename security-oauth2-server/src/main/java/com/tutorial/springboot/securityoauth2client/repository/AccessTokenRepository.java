package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.AccessToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {
}