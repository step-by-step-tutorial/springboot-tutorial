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

import java.util.List;


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
        var entity = (Permission) new EntityStubFactory().addPermission().get().asOne();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, permissionTransformer.toDto(entity));
    }

    public ResultHelper<List<Permission>, List<PermissionDto>> newTestPermission(int number) {
        var entities = (List<Permission>) new EntityStubFactory()
                .addPermission()
                .addPermission()
                .addPermission()
                .get()
                .asList();

        entities.forEach(entityManager::persist);
        entityManager.flush();
        var dtos = entities.stream()
                .map(permissionTransformer::toDto)
                .toList();

        return new ResultHelper<>(entities, dtos);
    }

    public ResultHelper<Permission, PermissionDto> fetchTestPermission() {
        var entity = entityManager.find(Permission.class, 1L);
        return new ResultHelper<>(entity, permissionTransformer.toDto(entity));
    }

    public ResultHelper<List<Permission>, List<PermissionDto>> fetchTestPermissions() {
        var query = entityManager.createQuery(
                "SELECT p FROM Permission p WHERE p.id IN :listOfId", Permission.class);
        query.setParameter("listOfId", List.of(1L, 2L));

        var entities = query.getResultList();
        var dtos = entities.stream()
                .map(permissionTransformer::toDto)
                .toList();

        return new ResultHelper<>(entities, dtos);
    }

    public ResultHelper<Role, RoleDto> newTestRole() {
        var entity = (Role) new EntityStubFactory().addRole().get().asOne();
        ;
        entityManager.persist(entity);
        return new ResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public ResultHelper<Role, RoleDto> newTestRoleIncludePermission() {
        var entity = (Role) new EntityStubFactory()
                .addPermission()
                .addRole()
                .get()
                .asOne();
        ;
        entityManager.persist(entity);
        return new ResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public ResultHelper<Role, RoleDto> fetchTestRole() {
        var entity = entityManager.find(Role.class, 1L);
        return new ResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public ResultHelper<User, UserDto> newTestUser() {
        var entity = (User) new EntityStubFactory().addUser().get().asOne();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public ResultHelper<User, UserDto> newTestUserIncludeRoleAndPermission() {
        var entity = (User) new EntityStubFactory()
                .addPermission()
                .addRole()
                .addUser()
                .get()
                .asOne();
        entityManager.persist(entity);
        return new ResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public ResultHelper<User, UserDto> fetchTestUser() {
        var entity = entityManager.find(User.class, 1L);
        return new ResultHelper<>(entity, userTransformer.toDto(entity));
    }

}
