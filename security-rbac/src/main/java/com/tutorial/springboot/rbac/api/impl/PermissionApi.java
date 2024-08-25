package com.tutorial.springboot.rbac.api.impl;

import com.tutorial.springboot.rbac.api.AllApi;
import com.tutorial.springboot.rbac.dto.PermissionDto;
import com.tutorial.springboot.rbac.entity.Permission;
import com.tutorial.springboot.rbac.service.impl.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionApi extends AllApi<Long, Permission, PermissionDto> {

    public PermissionApi(PermissionService service) {
        super(service);
    }
}

