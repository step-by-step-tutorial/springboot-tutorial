package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
