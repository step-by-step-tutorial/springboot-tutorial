package com.tutorial.springboot.security_authentication_inmemory.service;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserInitializerService implements CommandLineRunner {

    private final Logger logger = getLogger(this.getClass());

    private final UserService userService;

    public UserInitializerService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        logger.info("Initializing static users");
        userService.save(new UserDto().username("admin").password("password").roles(List.of("USER", "ADMIN")));
        userService.save(new UserDto().username("user").password("password").roles(List.of("USER")));
    }
}
