package com.tutorial.springboot.securityoauth2server.repository.rbac;

import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.repository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long>, RevisionRepository<Permission, Long, Long>, CustomRepository<Permission, Long> {
}
