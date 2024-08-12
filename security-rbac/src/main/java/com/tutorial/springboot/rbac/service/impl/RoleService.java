package com.tutorial.springboot.rbac.service.impl;

import com.tutorial.springboot.rbac.dto.RoleDto;
import com.tutorial.springboot.rbac.entity.Role;
import com.tutorial.springboot.rbac.repository.RoleRepository;
import com.tutorial.springboot.rbac.service.AbstractService;
import com.tutorial.springboot.rbac.service.BatchService;
import com.tutorial.springboot.rbac.service.CrudService;
import com.tutorial.springboot.rbac.transformer.RoleTransformer;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Long, Role, RoleDto> implements CrudService<Long, RoleDto>, BatchService<Long, RoleDto> {

    public RoleService(RoleRepository repository, RoleTransformer transformer) {
        super(repository, transformer);
    }
}
