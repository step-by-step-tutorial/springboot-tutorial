package com.tutorial.springboot.abac.api;

import com.tutorial.springboot.abac.dto.PermissionDto;
import com.tutorial.springboot.abac.entity.Permission;
import com.tutorial.springboot.abac.service.PermissionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionApi extends AbstractApi<Long, Permission, PermissionDto> {

    public PermissionApi(PermissionService service) {
        super(service);
    }
}

