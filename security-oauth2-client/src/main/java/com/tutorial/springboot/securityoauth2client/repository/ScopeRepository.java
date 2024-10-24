package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.Scope;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ScopeRepository extends JpaRepository<Scope, Long> {
    Optional<Scope> findByName(String name);
    Boolean existsByName(String name);
}













