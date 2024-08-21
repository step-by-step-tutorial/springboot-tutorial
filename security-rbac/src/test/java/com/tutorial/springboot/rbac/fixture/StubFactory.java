package com.tutorial.springboot.rbac.fixture;

public interface StubFactory {

    String TEST_USER_USERNAME = "test";

    String TEST_USER_PASSWORD = "test";

    String TEST_USER_EMAIL = "test@example.com";

    String TEST_ROLE_NAME = "ROLE_TEST";

    String TEST_PERMISSION_NAME = "TEST_PRIVILEGE";

    StubFactory addPermission();

    StubFactory addRole();

    StubFactory addUser();

    StubHelper<?> get();
}
