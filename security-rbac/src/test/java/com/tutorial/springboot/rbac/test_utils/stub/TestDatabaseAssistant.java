package com.tutorial.springboot.rbac.test_utils.stub;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.transformer.PermissionTransformer;
import com.tutorial.springboot.rbac.transformer.RoleTransformer;
import com.tutorial.springboot.rbac.transformer.UserTransformer;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Stream;

@Component
@Transactional
public class TestDatabaseAssistant {

    @Autowired
    EntityManager entityManager;

    @Autowired
    PermissionTransformer permissionTransformer;

    @Autowired
    RoleTransformer roleTransformer;

    @Autowired
    UserTransformer userTransformer;

    public ResultHelper<Permission, PermissionDto> insertTestPermission(int number) {
        var entity = EntityStubFactory.createPermission(number).asOne();
        entityManager.persist(entity);
        var dto = permissionTransformer.toDto(entity);
        return new ResultHelper<>(new StubHelper<>(entity), new StubHelper<>(dto));
    }

    public ResultHelper<Permission, PermissionDto> selectTestPermissions() {
        var query = entityManager.createQuery("SELECT p FROM Permission p WHERE p.id < 1000", Permission.class);

        var entities = query.getResultList().toArray(Permission[]::new);
        var dtoArray = Stream.of(entities).map(permissionTransformer::toDto).toArray(PermissionDto[]::new);

        return new ResultHelper<>(new StubHelper<>(entities), new StubHelper<>(dtoArray));
    }

    public ResultHelper<Role, RoleDto> insertTestRole(int number, int randomPermissionNumber) {
        var entity = EntityStubFactory.createRole(number, randomPermissionNumber).asOne();
        entityManager.persist(entity);
        var dto = roleTransformer.toDto(entity);

        return new ResultHelper<>(new StubHelper<>(entity), new StubHelper<>(dto));
    }

    public ResultHelper<Role, RoleDto> selectTestRole() {
        var query = entityManager.createQuery("SELECT p FROM Role p WHERE p.id < 1000", Role.class);

        var entities = query.getResultList().toArray(Role[]::new);
        var dtoList = Stream.of(entities).map(roleTransformer::toDto).toArray(RoleDto[]::new);

        return new ResultHelper<>(new StubHelper<>(entities), new StubHelper<>(dtoList));
    }

    public ResultHelper<User, UserDto> insertTestUser(int number, int randomRoleNumber, int randomPermissionNumber) {
        var entity = EntityStubFactory.createUser(number, randomRoleNumber, randomPermissionNumber).asOne();
        entityManager.persist(entity);
        var dto = userTransformer.toDto(entity);
        return new ResultHelper<>(new StubHelper<>(entity), new StubHelper<>(dto));
    }

    public ResultHelper<User, UserDto> selectTestUser() {
        var query = entityManager.createQuery("SELECT p FROM User p WHERE p.id < 1000", User.class);

        var entities = query.getResultList().toArray(User[]::new);
        var dtoList = Stream.of(entities).map(userTransformer::toDto).toArray(UserDto[]::new);

        return new ResultHelper<>(new StubHelper<>(entities), new StubHelper<>(dtoList));
    }

    public UserDto insertTestUserAndLogin() {
        var user = insertTestUser(1, 1, 1).dto().asOne();

        var auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));

        return user;
    }

}
