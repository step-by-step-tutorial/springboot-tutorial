package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PermissionRepository extends JpaRepository<Permission, Long>, CustomRepository<Permission, Long> {
    Optional<Permission> findByName(String name);

    Boolean existsByName(String name);
}
