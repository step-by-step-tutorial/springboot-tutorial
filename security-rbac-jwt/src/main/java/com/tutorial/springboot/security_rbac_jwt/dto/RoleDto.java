package com.tutorial.springboot.security_rbac_jwt.dto;

import com.tutorial.springboot.security_rbac_jwt.validation.SaveValidation;
import com.tutorial.springboot.security_rbac_jwt.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class RoleDto extends AbstractDto<Long, RoleDto> {

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "name should not be blank")
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
