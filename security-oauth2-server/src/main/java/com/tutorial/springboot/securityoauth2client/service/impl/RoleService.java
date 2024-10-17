package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.RoleDto;
import com.tutorial.springboot.securityoauth2client.entity.Role;
import com.tutorial.springboot.securityoauth2client.repository.RoleRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.service.BatchService;
import com.tutorial.springboot.securityoauth2client.service.CrudService;
import com.tutorial.springboot.securityoauth2client.transformer.RoleTransformer;
import org.springframework.stereotype.Service;

@Service
public class RoleService extends AbstractService<Long, Role, RoleDto> implements CrudService<Long, RoleDto>, BatchService<Long, RoleDto> {

    public RoleService(RoleRepository repository, RoleTransformer transformer) {
        super(repository, transformer);
    }
}
