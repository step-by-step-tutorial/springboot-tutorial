package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.repository.RoleRepository;
import com.tutorial.springboot.securityoauth2server.repository.UserRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.service.BatchService;
import com.tutorial.springboot.securityoauth2server.service.CrudService;
import com.tutorial.springboot.securityoauth2server.transformer.UserTransformer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.securityoauth2server.validation.ObjectValidation.shouldBeNotNullOrEmpty;

@Service
public class UserService extends AbstractService<Long, User, UserDto> implements CrudService<Long, UserDto>, BatchService<Long, UserDto> {

    private final PasswordEncoder passwordEncoder;

    private final RoleRepository roleRepository;

    public UserService(
            UserRepository repository,
            RoleRepository roleRepository,
            UserTransformer transformer,
            PasswordEncoder passwordEncoder
    ) {
        super(repository, transformer);
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDto findByUsername(String username) {
        shouldBeNotNullOrEmpty(username, "username is wrong");
        return ((UserRepository) repository).findByUsername(username)
                .map(transformer::toDto)
                .orElseThrow();
    }

    public void changePassword(String username, String currentPassword, String newPassword) {
        shouldBeNotNullOrEmpty(username, "credentials are wrong");
        shouldBeNotNullOrEmpty(currentPassword, "credentials are wrong");
        shouldBeNotNullOrEmpty(newPassword, "credentials are wrong");

        var user = ((UserRepository) repository).findByUsername(username).orElseThrow();

        if (user.getUsername().equals(username) && passwordEncoder.matches(currentPassword, user.getPassword())) {
            user.setPassword(passwordEncoder.encode(newPassword));
            repository.save(user);
        } else {
            throw new RuntimeException("Password could not be changed, due to incorrect password");
        }
    }

    @Override
    protected void beforeSave(UserDto dto, User entity) {
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));

        var roles = roleRepository.findOrSaveAll(entity.getRoles());
        entity.getRoles().clear();
        entity.getRoles().addAll(roles);
    }

}
