package com.tutorial.springboot.rbac.service.impl;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.repository.UserRepository;
import com.tutorial.springboot.rbac.service.AbstractService;
import com.tutorial.springboot.rbac.service.BatchService;
import com.tutorial.springboot.rbac.service.CrudService;
import com.tutorial.springboot.rbac.transformer.UserTransformer;
import org.springframework.security.core.context.SecurityContextHolder;
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

        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            repository.save(user);
        } else {
            throw new RuntimeException("Password could not be changed, due to incorrect password.");
        }
    }
}
