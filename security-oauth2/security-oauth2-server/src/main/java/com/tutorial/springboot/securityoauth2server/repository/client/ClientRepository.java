package com.tutorial.springboot.securityoauth2server.repository.client;

import com.tutorial.springboot.securityoauth2server.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long>, RevisionRepository<Client, Long, Long> {
    Optional<Client> findByClientId(String clientId);
}