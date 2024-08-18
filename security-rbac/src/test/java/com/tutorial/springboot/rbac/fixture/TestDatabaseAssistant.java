package com.tutorial.springboot.rbac.fixture;

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
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.tutorial.springboot.rbac.fixture.EntityFixture.*;

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

    public ResultHelper<Permission, PermissionDto> newTestPermission() {
        var entity = EntityFixture.createTestPermission();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, permissionTransformer.toDto(entity));
    }

    public ResultHelper<Permission, PermissionDto> fetchTestPermission() {
            var entity = entityManager.find(Permission.class, 1L);
            return new ResultHelper<>(entity, permissionTransformer.toDto(entity));
    }

    public ResultHelper<Role, RoleDto> newTestRole() {
        var entity = EntityFixture.createTestRole();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public ResultHelper<Role, RoleDto> newTestRoleIncludePermission() {
        var entity = EntityFixture.createTestRoleIncludePermission();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public ResultHelper<Role, RoleDto> fetchTestRole() {
            var entity = entityManager.find(Role.class, 1L);
            return new ResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public ResultHelper<User, UserDto> newTestUser() {
        var entity = createTestUser();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public ResultHelper<User, UserDto> newTestUserIncludeRoleAndPermission() {
        var entity = EntityFixture.createTestUserIncludeRoleAndPermission();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public ResultHelper<User, UserDto> fetchTestUser() {
            var entity = entityManager.find(User.class, 1L);
            return new ResultHelper<>(entity, userTransformer.toDto(entity));
    }

}
