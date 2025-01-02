package com.tutorial.springboot.security_rbac_jwt.repository;

import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, RevisionRepository<Role, Long, Long>, CustomRepository<Role, Long> {
}
