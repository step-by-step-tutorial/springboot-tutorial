package com.tutorial.springboot.securityoauth2server.repository;

import com.tutorial.springboot.securityoauth2server.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface RoleRepository extends JpaRepository<Role, Long>, RevisionRepository<Role, Long, Long>, CustomRepository<Role, Long> {
}
