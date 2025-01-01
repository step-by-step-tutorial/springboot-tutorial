package com.tutorial.springboot.securityoauth2server.dto;

import com.tutorial.springboot.securityoauth2server.validation.SaveValidation;
import com.tutorial.springboot.securityoauth2server.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class RoleDto extends CodeTableDto<Long, RoleDto> {

    private List<PermissionDto> permissions = new ArrayList<>();

    public List<PermissionDto> getPermissions() {
        return permissions;
    }

    public RoleDto setPermissions(List<PermissionDto> permissions) {
        this.permissions = permissions;
        return this;
    }
}
