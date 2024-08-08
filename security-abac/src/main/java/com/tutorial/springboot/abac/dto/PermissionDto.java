package com.tutorial.springboot.abac.dto;

public class PermissionDto extends AbstractDto<Long, PermissionDto> {

    private String name;

    public String getName() {
        return name;
    }

    public PermissionDto setName(String name) {
        this.name = name;
        return this;
    }
}
