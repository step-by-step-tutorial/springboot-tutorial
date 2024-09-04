package com.tutorial.springboot.securityoauth2server.repository;

import com.tutorial.springboot.securityoauth2server.entity.OAuthResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthResourceRepository extends JpaRepository<OAuthResource, Long> {
}
