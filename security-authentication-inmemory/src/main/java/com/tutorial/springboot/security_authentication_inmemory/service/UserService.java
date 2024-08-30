package com.tutorial.springboot.security_authentication_inmemory.service;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import com.tutorial.springboot.security_authentication_inmemory.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class UserService {

    private final Logger logger = getLogger(this.getClass());

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        initialWithStaticUsers();
    }

    private void initialWithStaticUsers() {
        logger.info("Initializing static users");
        save(new UserDto().username("user").password("password").roles(List.of("USER")));
        save(new UserDto().username("admin").password("password").roles(List.of("USER", "ADMIN")));
    }

    public Optional<Boolean> save(UserDto newUser) {
        var user = User.builder()
                .username(newUser.username())
                .passwordEncoder(passwordEncoder::encode)
                .password(newUser.password())
                .roles(newUser.roles().toArray(String[]::new))
                .build();

        userRepository.createUser(user);
        logger.info("Create newUser: {}", user.getUsername());
        return Optional.of(true);
    }

    public String currentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
