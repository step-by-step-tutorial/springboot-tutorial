package com.tutorial.springboot.security_authentication_inmemory.service;

import com.tutorial.springboot.security_authentication_inmemory.repository.UserRepository;
import org.slf4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class RoleService {

    private final Logger logger = getLogger(this.getClass());

    private final UserRepository userRepository;

    public RoleService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void update(String username, String... roles) {
        var user = userRepository.loadUserByUsername(username);
        var currentRoles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        var currentAndNewRoles = new HashSet<>(List.of(roles));
        currentAndNewRoles.addAll(currentRoles);

        userRepository.updateUser(User.withUserDetails(user).authorities(currentAndNewRoles.stream().toArray(String[]::new)).build());
        logger.info("Roles of [{}] updated", username);
    }

    public List<String> findRolesByUsername(String username) {
        return userRepository.loadUserByUsername(username)
                .getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }

    public List<String> currentRoles() {
        return findRolesByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
    }

}
