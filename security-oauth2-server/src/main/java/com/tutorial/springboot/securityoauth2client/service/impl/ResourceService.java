package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.ResourceDto;
import com.tutorial.springboot.securityoauth2client.entity.Resource;
import com.tutorial.springboot.securityoauth2client.repository.ResourceRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.transformer.ResourceTransformer;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<Long, Resource, ResourceDto> {

    public ResourceService(ResourceRepository repository, ResourceTransformer transformer) {
        super(repository, transformer);
    }

}
