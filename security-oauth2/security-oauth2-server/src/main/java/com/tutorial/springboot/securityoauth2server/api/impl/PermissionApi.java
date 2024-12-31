package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.api.AllApi;
import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.service.impl.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionApi extends AllApi<Long, Permission, PermissionDto> {

    public PermissionApi(PermissionService service) {
        super(service);
    }

    @Override
    protected PermissionService getService() {
        return (PermissionService) service;
    }
}

