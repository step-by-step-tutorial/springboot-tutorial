package com.tutorial.springboot.securityoauth2server.dto;

import com.tutorial.springboot.securityoauth2server.validation.SaveValidation;
import com.tutorial.springboot.securityoauth2server.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.List;

public class RoleDto extends AbstractDto<Long, RoleDto> {

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "name should not be blank")
    private String name;

    private String description;

    private List<PermissionDto> permissions = new ArrayList<>();

    public String getName() {
        return name;
    }

    public RoleDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public RoleDto setDescription(String description) {
        this.description = description;
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
