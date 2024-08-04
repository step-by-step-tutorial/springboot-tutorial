package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.convertor.UserConverter;
import com.tutorial.springboot.abac.dto.UserDto;
import com.tutorial.springboot.abac.model.User;
import com.tutorial.springboot.abac.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User findByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(String.format("User %s not found", username)));
    }

    public void create(UserDetails user) {
        userRepository.save((User) user);
    }

    public void update(UserDetails user) {
        userRepository.save((User) user);
    }

    public void delete(String username) {
        var user = findByUsername(username);
        userRepository.delete(user);
    }

    public void changePassword(String oldPassword, String newPassword) {
        var user = findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        user.setPassword(newPassword);
        userRepository.save(user);
    }

    public boolean exists(String username) {
        return userRepository.findByUsername(username).isPresent();
    }

    public List<UserDto> getAll() {
        return userRepository.findAll().stream()
                .map(UserConverter::toDto)
                .collect(Collectors.toList());
    }

    public Optional<UserDto> getById(Long id) {
        return userRepository.findById(id).map(UserConverter::toDto);
    }

    public UserDto create(UserDto userDto) {
        User user = UserConverter.toEntity(userDto);
        user = userRepository.save(user);
        return UserConverter.toDto(user);
    }

    public Optional<UserDto> update(Long id, UserDto userDto) {
        if (userRepository.existsById(id)) {
            User user = UserConverter.toEntity(userDto);
            user.setId(id);
            user = userRepository.save(user);
            return Optional.of(UserConverter.toDto(user));
        }
        return Optional.empty();
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public User extract(Authentication auth) {
        var username = ((UserDetails) auth.getPrincipal()).getUsername();
        return findByUsername(username);
    }

}
