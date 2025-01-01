package com.tutorial.springboot.securityoauth2server.repository.rbac;

import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.repository.CustomRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, RevisionRepository<Role, Long, Long>, CustomRepository<Role, Long> {
}
