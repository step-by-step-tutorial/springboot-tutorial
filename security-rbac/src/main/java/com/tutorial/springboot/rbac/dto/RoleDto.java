package com.tutorial.springboot.rbac.dto;

import java.util.ArrayList;
import java.util.List;

public class RoleDto extends AbstractDto<Long, RoleDto> {

    private String name;

    private List<PermissionDto> permissions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public RoleDto setName(String name) {
        this.name = name;
        return this;
    }

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public RoleDto setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
        return this;
    }
}
