package com.tutorial.springboot.security_rbac_jwt.service.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.repository.RoleRepository;
import com.tutorial.springboot.security_rbac_jwt.repository.UserRepository;
import com.tutorial.springboot.security_rbac_jwt.service.AbstractService;
import com.tutorial.springboot.security_rbac_jwt.service.BatchService;
import com.tutorial.springboot.security_rbac_jwt.service.CrudService;
import com.tutorial.springboot.security_rbac_jwt.transformer.UserTransformer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.tutorial.springboot.security_rbac_jwt.util.SecurityUtils.getCurrentUsername;
import static com.tutorial.springboot.security_rbac_jwt.validation.ObjectValidation.shouldBeNotNullOrEmpty;

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

        var roles = roleRepository.findOrSaveAll(entity.getRoles());
        entity.getRoles().clear();
        entity.getRoles().addAll(roles);
    }

}
