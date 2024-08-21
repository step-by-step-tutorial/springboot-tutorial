package com.tutorial.springboot.rbac.fixture;

import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.User;

import java.util.ArrayList;
import java.util.List;

public class EntityStubFactory implements StubFactory {

    private final List<Permission> permissionStubs = new ArrayList<>();

    private final List<Role> roleStubs = new ArrayList<>();

    private final List<User> userStubs = new ArrayList<>();
    
    public EntityStubFactory() {
    }

    @Override
    public StubFactory addPermission() {
        permissionStubs.add(new Permission().setName(TEST_PERMISSION_NAME + "_" + permissionStubs.size()));
        return this;
    }

    @Override
    public StubFactory addRole() {
        roleStubs.add(new Role()
                .setName(TEST_ROLE_NAME + "_" + roleStubs.size())
                .setPermissions(permissionStubs));
        return this;
    }

    @Override
    public StubFactory addUser() {
        userStubs.add(new User()
                .setUsername(TEST_USER_USERNAME + "_" + userStubs.size())
                .setPassword(TEST_USER_PASSWORD)
                .setEmail(TEST_USER_USERNAME + "_" + userStubs.size() + "@example.com")
                .setEnabled(true)
                .setRoles(roleStubs));
        return this;
    }

    @Override
    public StubHelper<?> get() {
        if (!userStubs.isEmpty()) return new StubHelper<>(userStubs);
        else if (!roleStubs.isEmpty()) return new StubHelper<>(roleStubs);
        else return new StubHelper<>(permissionStubs);
    }
}