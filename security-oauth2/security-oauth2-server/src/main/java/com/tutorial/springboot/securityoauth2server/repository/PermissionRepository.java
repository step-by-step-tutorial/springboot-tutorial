package com.tutorial.springboot.securityoauth2server.repository;

import com.tutorial.springboot.securityoauth2server.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    Optional<Permission> findByName(String name);

    Boolean existsByName(String name);
}
