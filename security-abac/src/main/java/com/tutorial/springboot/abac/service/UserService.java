package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.dto.UserDto;
import com.tutorial.springboot.abac.entity.User;
import com.tutorial.springboot.abac.repository.UserRepository;
import com.tutorial.springboot.abac.transformer.UserTransformer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractService<Long, User, UserDto> implements CrudService<Long, UserDto>, BatchService<Long, UserDto> {

    public UserService(UserRepository repository, UserTransformer transformer) {
        super(repository, transformer);
    }

    public User findByUsername(String username) {
        return ((UserRepository) repository).findByUsername(username).orElseThrow(() -> new RuntimeException(String.format("User %s not found", username)));
    }

    public void changePassword(String oldPassword, String newPassword) {
        var user = findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!user.getPassword().equals(oldPassword)) {
            throw new RuntimeException("There is a problem, please try again or later");
        }
        user.setPassword(newPassword);
        repository.save(user);
    }

    public User extract(Authentication auth) {
        var username = ((UserDetails) auth.getPrincipal()).getUsername();
        return findByUsername(username);
    }

}
