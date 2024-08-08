package com.tutorial.springboot.abac.dto;

import java.util.List;

public class RoleDto extends AbstractDto<Long, RoleDto> {
    private String authority;

    private List<PermissionDto> permissions;

    public String getAuthority() {
        return authority;
    }

    public RoleDto setAuthority(String authority) {
        this.authority = authority;
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
