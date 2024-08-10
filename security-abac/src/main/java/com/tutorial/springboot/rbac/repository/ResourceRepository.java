package com.tutorial.springboot.rbac.repository;

import com.tutorial.springboot.rbac.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
