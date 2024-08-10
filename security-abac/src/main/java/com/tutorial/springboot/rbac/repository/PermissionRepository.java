package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
