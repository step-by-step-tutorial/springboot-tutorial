package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.OAuthResourceDto;
import com.tutorial.springboot.securityoauth2server.entity.OAuthResource;
import com.tutorial.springboot.securityoauth2server.repository.OAuthResourceRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.OAuthResourceTransformer;
import org.springframework.stereotype.Service;

@Service
public class OAuthResourceService extends AbstractService<Long, OAuthResource, OAuthResourceDto> {

    public OAuthResourceService(OAuthResourceRepository repository, OAuthResourceTransformer transformer) {
        super(repository, transformer);
    }

}
