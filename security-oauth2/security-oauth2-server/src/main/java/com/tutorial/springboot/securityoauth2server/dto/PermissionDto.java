package com.tutorial.springboot.securityoauth2server.dto;


import com.tutorial.springboot.securityoauth2server.validation.SaveValidation;
import com.tutorial.springboot.securityoauth2server.validation.UpdateValidation;
import jakarta.validation.constraints.NotBlank;

public class PermissionDto extends AbstractDto<Long, PermissionDto> {

    @NotBlank(groups = {SaveValidation.class, UpdateValidation.class}, message = "name should not be blank")
    private String name;

    public String getName() {
        return name;
    }

    public PermissionDto setName(String name) {
        this.name = name;
        return this;
    }
}
