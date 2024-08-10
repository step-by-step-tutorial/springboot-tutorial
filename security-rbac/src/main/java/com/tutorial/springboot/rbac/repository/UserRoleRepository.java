package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
