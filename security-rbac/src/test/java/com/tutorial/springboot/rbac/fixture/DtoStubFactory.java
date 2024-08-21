package com.tutorial.springboot.rbac.fixture;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class DtoStubFactory implements StubFactory {

    private final List<PermissionDto> permissions = new ArrayList<>();

    private final List<RoleDto> roles = new ArrayList<>();

    private final List<UserDto> users = new ArrayList<>();

    public DtoStubFactory() {
    }

    @Override
    public DtoStubFactory addPermission() {
        permissions.add(new PermissionDto().setName(TEST_PERMISSION_NAME + "_" + permissions.size()));
        return this;
    }

    @Override
    public DtoStubFactory addRole() {
        roles.add(new RoleDto()
                .setName(TEST_ROLE_NAME + "_" + roles.size())
                .setPermissions(permissions));
        return this;
    }

    @Override
    public DtoStubFactory addUser() {
        users.add(new UserDto()
                .setUsername(TEST_USER_USERNAME + "_" + users.size())
                .setPassword(TEST_USER_PASSWORD)
                .setEmail(TEST_USER_USERNAME + "_" + users.size() + "@example.com")
                .setEnabled(true)
                .setRoles(roles));
        return this;
    }

    @Override
    public StubHelper<?> get() {
        if (!users.isEmpty()) return new StubHelper<>(users);
        else if (!roles.isEmpty()) return new StubHelper<>(roles);
        else return  new StubHelper<>(permissions);
    }
}