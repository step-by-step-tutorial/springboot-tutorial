package com.tutorial.springboot.security_authentication_inmemory.repository;

import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository extends InMemoryUserDetailsManager {

}
