package com.tutorial.springboot.rbac.service.impl;

import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.repository.UserRepository;
import com.tutorial.springboot.rbac.service.AbstractService;
import com.tutorial.springboot.rbac.service.BatchService;
import com.tutorial.springboot.rbac.service.CrudService;
import com.tutorial.springboot.rbac.transformer.UserTransformer;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.rbac.util.SecurityUtils.getCurrentUsername;
import static com.tutorial.springboot.rbac.validation.ObjectValidation.shouldBeNotNullOrEmpty;

@Service
public class UserService extends AbstractService<Long, User, UserDto> implements CrudService<Long, UserDto>, BatchService<Long, UserDto> {

    public UserService(UserRepository repository, UserTransformer transformer) {
        super(repository, transformer);
    }

    public User findByUsername(String username) {
        shouldBeNotNullOrEmpty(username, "username is wrong");
        return ((UserRepository) repository).findByUsername(username)
                .orElseThrow();
    }

    public void changePassword(String oldPassword, String newPassword) {
        shouldBeNotNullOrEmpty(oldPassword, "password is wrong");
        shouldBeNotNullOrEmpty(newPassword, "password is wrong");

        var user = findByUsername(getCurrentUsername());

        if (user.getPassword().equals(oldPassword)) {
            user.setPassword(newPassword);
            repository.save(user);
        } else {
            throw new RuntimeException("Password could not be changed, due to incorrect password");
        }
    }
}
