package com.tutorial.springboot.securityoauth2server.repository.resource;

import com.tutorial.springboot.securityoauth2server.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long>, RevisionRepository<Resource, Long, Long> {
}
