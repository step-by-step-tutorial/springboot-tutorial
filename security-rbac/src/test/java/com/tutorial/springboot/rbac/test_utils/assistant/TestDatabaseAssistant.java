package com.tutorial.springboot.rbac.test_utils.assistant;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.dto.UserDto;
import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.User;
import com.tutorial.springboot.rbac.test_utils.stub.EntityStubFactory;
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

    public AssistantResultHelper<Permission, PermissionDto> insertTestPermission() {
        var entity = EntityStubFactory.createPermission(1).asOne();
        entityManager.persist(entity);
        return new AssistantResultHelper<>(entity, permissionTransformer.toDto(entity));
    }

    public AssistantResultHelper<List<Permission>, List<PermissionDto>> insertTestPermission(int number) {
        var entities = EntityStubFactory.createPermission(number).asList();

        entities.forEach(entityManager::persist);
        entityManager.flush();

        var dtoList = entities.stream().map(permissionTransformer::toDto).toList();

        return new AssistantResultHelper<>(entities, dtoList);
    }

    public AssistantResultHelper<Permission, PermissionDto> selectTestPermission() {
        var entity = entityManager.find(Permission.class, 1L);
        return new AssistantResultHelper<>(entity, permissionTransformer.toDto(entity));
    }

    public AssistantResultHelper<List<Permission>, List<PermissionDto>> selectAllTestPermissions() {
        var query = entityManager.createQuery("SELECT p FROM Permission p WHERE p.id < 1000", Permission.class);

        var entities = query.getResultList();
        var dtoList = entities.stream().map(permissionTransformer::toDto).toList();

        return new AssistantResultHelper<>(entities, dtoList);
    }

    public AssistantResultHelper<Role, RoleDto> insertTestRole() {
        var entity = EntityStubFactory.createRole(1, 0).asOne();
        entityManager.persist(entity);
        return new AssistantResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public AssistantResultHelper<Role, RoleDto> insertComplexTestRole() {
        var entity = EntityStubFactory.createRole(1, 1).asOne();
        entityManager.persist(entity);
        return new AssistantResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public AssistantResultHelper<Role, RoleDto> selectTestRole() {
        var entity = entityManager.find(Role.class, 1L);
        return new AssistantResultHelper<>(entity, roleTransformer.toDto(entity));
    }

    public AssistantResultHelper<User, UserDto> insertTestUser() {
        var entity = EntityStubFactory.createUser(1, 0, 0).asOne();
        entityManager.persist(entity);
        return new AssistantResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public AssistantResultHelper<User, UserDto> insertComplexTestUser() {
        var entity = EntityStubFactory.createUser(1, 1, 1).asOne();
        entityManager.persist(entity);
        return new AssistantResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public AssistantResultHelper<User, UserDto> selectTestUser() {
        var entity = entityManager.find(User.class, 1L);
        return new AssistantResultHelper<>(entity, userTransformer.toDto(entity));
    }

    public UserDto insertTestUserAndLogin() {
        var user = insertTestUser().asDto;

        var auth = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));

        return user;
    }

}
