package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.repository.RoleRepository;
import com.tutorial.springboot.securityoauth2server.repository.UserRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.service.BatchService;
import com.tutorial.springboot.securityoauth2server.service.CrudService;
import com.tutorial.springboot.securityoauth2server.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;
import static com.tutorial.springboot.securityoauth2server.validation.ObjectValidation.shouldBeNotNullOrEmpty;

@Service
public class UserService extends AbstractService<Long, User, UserDto> implements CrudService<Long, UserDto>, BatchService<Long, UserDto> {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

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

    @Override
    protected void beforeSave(UserDto dto, User entity) {
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        var roles = entity.getRoles()
                .stream()
                .map(role -> {
                    if (roleRepository.existsByName(role.getName())) {
                        return roleRepository.findByName(role.getName()).get();
                    } else {
                        return role;
                    }
                })
                .toList();
        entity.setRoles(roles);
    }
}
