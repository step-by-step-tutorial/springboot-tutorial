package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.ResourceDto;
import com.tutorial.springboot.securityoauth2server.entity.Resource;
import com.tutorial.springboot.securityoauth2server.repository.resource.ResourceRepository;
import com.tutorial.springboot.securityoauth2server.repository.ScopeRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.ResourceTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<Long, Resource, ResourceDto> {

    @Autowired
    private ScopeRepository scopeRepository;

    public ResourceService(ResourceRepository repository, ResourceTransformer transformer) {
        super(repository, transformer);
    }

    @Override
    protected void beforeSave(ResourceDto dto, Resource entity) {
        var scopes = scopeRepository.findOrSaveAll(entity.getScopes());
        entity.getScopes().clear();
        entity.getScopes().addAll(scopes);

    }
}
