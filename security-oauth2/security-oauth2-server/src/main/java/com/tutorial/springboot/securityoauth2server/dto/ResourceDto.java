package com.tutorial.springboot.securityoauth2server.dto;

import java.util.List;

public class ResourceDto extends CodeTableDto<Long, ResourceDto> {

    private List<String> scopes;

    public List<String> getScopes() {
        return scopes;
    }

    public ResourceDto setScopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }
}
