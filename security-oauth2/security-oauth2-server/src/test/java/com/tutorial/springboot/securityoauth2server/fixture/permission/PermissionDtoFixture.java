package com.tutorial.springboot.securityoauth2server.fixture.permission;

import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;

public final class PermissionDtoFixture {

    private PermissionDtoFixture() {
    }

    public static PermissionDto newGivenPermission() {
        return new PermissionDto()
                .setName("permission")
                .setDescription("permission description")
                .setVersion(0);
    }

    public static PermissionDto newGivenPermission(String name) {
        return new PermissionDto()
                .setName(name)
                .setDescription(name + "description")
                .setVersion(0);
    }
}