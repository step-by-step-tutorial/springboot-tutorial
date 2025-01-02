package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long>, RevisionRepository<Permission, Long, Long>, CustomRepository<Permission, Long> {
}
