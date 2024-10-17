package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
