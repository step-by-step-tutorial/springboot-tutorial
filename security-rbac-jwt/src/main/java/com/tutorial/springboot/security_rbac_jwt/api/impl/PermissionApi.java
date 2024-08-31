package com.tutorial.springboot.security_rbac_jwt.api.impl;

import com.tutorial.springboot.security_rbac_jwt.api.AllApi;
import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionApi extends AllApi<Long, Permission, PermissionDto> {

    public PermissionApi(PermissionService service) {
        super(service);
    }
}

