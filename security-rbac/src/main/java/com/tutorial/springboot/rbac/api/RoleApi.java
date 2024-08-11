package com.tutorial.springboot.rbac.api;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.service.impl.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleApi extends AbstractApi<Long, Role, RoleDto> {

    public RoleApi(RoleService service) {
        super(service);
    }
}
