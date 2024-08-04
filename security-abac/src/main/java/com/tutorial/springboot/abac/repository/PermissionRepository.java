package com.tutorial.springboot.abac.repository;

import com.tutorial.springboot.abac.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
