package com.tutorial.springboot.rbac.fixture;

import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.entity.User;

import java.util.List;
import java.util.stream.IntStream;

public final class EntityFixture implements Fixture {

    private EntityFixture() {
    }

    public static Permission createTestPermission() {
        return new Permission()
                .setName(TEST_PERMISSION_NAME);
    }

    public static List<Permission> createMultiTestPermission(int numberOfPermissions) {
        return IntStream.range(0, numberOfPermissions)
                .boxed()
                .map(index -> new Permission().setName(TEST_PERMISSION_NAME + index))
                .toList();
    }

    public static Role createTestRole() {
        return new Role()
                .setName(TEST_ROLE_NAME);
    }

    public static List<Role> createMultiTestRole(int numberOfRoles) {
        return IntStream.range(0, numberOfRoles)
                .boxed()
                .map(index -> new Role().setName(TEST_ROLE_NAME))
                .toList();
    }

    public static Role createTestRoleIncludePermission() {
        return createTestRole().setPermissions(createMultiTestPermission(2));
    }

    public static User createTestUser() {
        return new User()
                .setUsername(TEST_USER_USERNAME)
                .setPassword(TEST_USER_PASSWORD)
                .setEmail(TEST_USER_EMAIL)
                .setEnabled(true);
    }

    public static List<User> createMultipleTestUser(int numberOfUsers) {
        return IntStream.range(0, numberOfUsers)
                .boxed()
                .map(index -> new User()
                        .setUsername(TEST_USER_USERNAME + index)
                        .setPassword(TEST_USER_PASSWORD + index)
                        .setEmail(TEST_USER_USERNAME + index + "@example.com")
                        .setEnabled(true))
                .toList();
    }

    public static User createTestUserIncludeRoleAndPermission() {
        return createTestUser()
                .addRole(createTestRole().addPermissions(createTestPermission()));
    }
}