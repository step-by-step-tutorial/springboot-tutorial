package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.ScopeDto;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import com.tutorial.springboot.securityoauth2server.repository.ScopeRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.ScopeTransformer;
import org.springframework.stereotype.Service;

@Service
public class ScopeService extends AbstractService<Long, Scope, ScopeDto> {

    public ScopeService(ScopeRepository repository, ScopeTransformer transformer) {
        super(repository, transformer);
    }

}
