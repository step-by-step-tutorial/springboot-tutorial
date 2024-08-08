package com.tutorial.springboot.abac.service;

import com.tutorial.springboot.abac.dto.RoleDto;
import com.tutorial.springboot.abac.entity.Role;
import com.tutorial.springboot.abac.repository.RoleRepository;
import com.tutorial.springboot.abac.transformer.RoleTransformer;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Long, Role, RoleDto> implements CrudService<Long, RoleDto>, BatchService<Long, RoleDto> {

    public RoleService(RoleRepository repository, RoleTransformer transformer) {
        super(repository, transformer);
    }
}
