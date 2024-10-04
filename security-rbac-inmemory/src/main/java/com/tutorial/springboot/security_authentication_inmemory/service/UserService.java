package com.tutorial.springboot.security_authentication_inmemory.service;

import com.tutorial.springboot.security_authentication_inmemory.dto.UserDto;
import com.tutorial.springboot.security_authentication_inmemory.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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
    }

    public void save(UserDto dto) {
        var entity = User.builder()
                .username(dto.username())
                .passwordEncoder(passwordEncoder::encode)
                .password(dto.password())
                .roles(dto.roles().toArray(String[]::new))
                .build();

        userRepository.createUser(entity);
        logger.info("Create newUser: {}", entity.getUsername());
    }

    public Optional<UserDto> findByUsername(String username) {
        var entity = userRepository.loadUserByUsername(username);
        var dto = new UserDto()
                .username(entity.getUsername())
                .roles(entity.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

        return Optional.of(dto);
    }

    public String currentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
