package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
