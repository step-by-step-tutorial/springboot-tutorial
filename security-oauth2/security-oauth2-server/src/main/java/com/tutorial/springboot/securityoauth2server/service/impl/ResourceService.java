package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.ResourceDto;
import com.tutorial.springboot.securityoauth2server.entity.Resource;
import com.tutorial.springboot.securityoauth2server.repository.ResourceRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.ResourceTransformer;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<Long, Resource, ResourceDto> {

    public ResourceService(ResourceRepository repository, ResourceTransformer transformer) {
        super(repository, transformer);
    }

}
