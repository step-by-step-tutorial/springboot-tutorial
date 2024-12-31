package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.api.AllApi;
import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.service.impl.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleApi extends AllApi<Long, Role, RoleDto> {

    public RoleApi(RoleService service) {
        super(service);
    }

    @Override
    protected RoleService getService() {
        return (RoleService) service;
    }
}
