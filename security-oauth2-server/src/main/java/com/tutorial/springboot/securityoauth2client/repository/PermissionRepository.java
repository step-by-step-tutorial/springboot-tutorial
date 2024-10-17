package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
}
