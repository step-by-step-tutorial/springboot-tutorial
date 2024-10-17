package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}