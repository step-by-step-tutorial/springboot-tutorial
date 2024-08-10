package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
}
