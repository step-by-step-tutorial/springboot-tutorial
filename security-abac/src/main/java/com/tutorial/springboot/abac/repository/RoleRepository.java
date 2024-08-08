package com.tutorial.springboot.abac.repository;

import com.tutorial.springboot.abac.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
