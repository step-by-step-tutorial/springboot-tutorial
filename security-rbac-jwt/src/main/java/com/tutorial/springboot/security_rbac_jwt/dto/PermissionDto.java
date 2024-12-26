package com.tutorial.springboot.security_rbac_jwt.dto;


import com.tutorial.springboot.security_rbac_jwt.validation.SaveValidation;
import com.tutorial.springboot.security_rbac_jwt.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;

public class PermissionDto extends AbstractDto<Long, PermissionDto> {

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "name should not be blank")
    private String name;

    private String description;

    public String getName() {
        return name;
    }

    public PermissionDto setName(String name) {
        this.name = name;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public PermissionDto setDescription(String description) {
        this.description = description;
        return this;
    }
}
