package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.UserDto;
import com.tutorial.springboot.securityoauth2client.entity.User;
import com.tutorial.springboot.securityoauth2client.repository.UserRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.service.BatchService;
import com.tutorial.springboot.securityoauth2client.service.CrudService;
import com.tutorial.springboot.securityoauth2client.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.securityoauth2client.util.SecurityUtils.getCurrentUsername;
import static com.tutorial.springboot.securityoauth2client.validation.ObjectValidation.shouldBeNotNullOrEmpty;

@Service
public class UserService extends AbstractService<Long, User, UserDto> implements CrudService<Long, UserDto>, BatchService<Long, UserDto> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, UserTransformer transformer) {
        super(repository, transformer);
    }

    @Override
    protected void beforeSave(UserDto dto, User entity) {
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
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
