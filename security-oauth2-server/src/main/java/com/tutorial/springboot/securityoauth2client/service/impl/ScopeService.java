package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.ScopeDto;
import com.tutorial.springboot.securityoauth2client.entity.Scope;
import com.tutorial.springboot.securityoauth2client.repository.ScopeRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.transformer.ScopeTransformer;
import org.springframework.stereotype.Service;

@Service
public class ScopeService extends AbstractService<Long, Scope, ScopeDto> {

    public ScopeService(ScopeRepository repository, ScopeTransformer transformer) {
        super(repository, transformer);
    }

}
