package com.tutorial.springboot.securityoauth2server.repository;

import com.tutorial.springboot.securityoauth2server.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long>, RevisionRepository<Permission, Long, Long>, CustomRepository<Permission, Long> {
}
