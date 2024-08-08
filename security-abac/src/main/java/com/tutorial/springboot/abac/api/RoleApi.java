package com.tutorial.springboot.abac.api;

import com.tutorial.springboot.abac.dto.RoleDto;
import com.tutorial.springboot.abac.entity.Role;
import com.tutorial.springboot.abac.service.RoleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleApi extends AbstractApi<Long, Role, RoleDto> {

    public RoleApi(RoleService service) {
        super(service);
    }
}
