package com.tutorial.springboot.rbac.fixture;

import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.dto.UserDto;

import java.util.List;
import java.util.stream.IntStream;

public final class DtoFixture implements Fixture {

    private DtoFixture() {
    }

    public static PermissionDto createTestPermission() {
        return new PermissionDto()
                .setName(Fixture.TEST_PERMISSION_NAME);
    }

    public static List<PermissionDto> createMultiTestPermission(int numberOfPermissions) {
        return IntStream.range(0, numberOfPermissions)
                .boxed()
                .map(index -> new PermissionDto().setName(Fixture.TEST_PERMISSION_NAME + index))
                .toList();
    }

    public static RoleDto createTestRole() {
        return new RoleDto()
                .setName(Fixture.TEST_ROLE_NAME);
    }

    public static List<RoleDto> createMultiTestRole(int numberOfRoles) {
        return IntStream.range(0, numberOfRoles)
                .boxed()
                .map(index -> new RoleDto().setName(Fixture.TEST_ROLE_NAME))
                .toList();
    }

    public static UserDto createTestUser() {
        return new UserDto()
                .setUsername(Fixture.TEST_USER_USERNAME)
                .setPassword(Fixture.TEST_USER_PASSWORD)
                .setEmail(Fixture.TEST_USER_EMAIL)
                .setEnabled(true);
    }

    public static List<UserDto> createMultipleTestUser(int numberOfUsers) {
        return IntStream.range(0, numberOfUsers)
                .boxed()
                .map(index -> new UserDto()
                        .setUsername(Fixture.TEST_USER_USERNAME + index)
                        .setPassword(Fixture.TEST_USER_PASSWORD + index)
                        .setEmail(Fixture.TEST_USER_USERNAME + index + "@example.com")
                        .setEnabled(true))
                .toList();
    }

    public static UserDto createTestUserIncludeRoleAndPermission() {
        return createTestUser()
                .setRoles(List.of(createTestRole().setPermissions(List.of(createTestPermission()))));
    }
}