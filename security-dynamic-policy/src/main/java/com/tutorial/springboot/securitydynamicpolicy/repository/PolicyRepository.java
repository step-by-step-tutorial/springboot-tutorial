package com.tutorial.springboot.securitydynamicpolicy.repository;

import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
}
