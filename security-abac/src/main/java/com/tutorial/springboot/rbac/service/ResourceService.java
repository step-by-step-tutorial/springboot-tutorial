package com.tutorial.springboot.rbac.service;

import com.tutorial.springboot.rbac.dto.ResourceDto;
import com.tutorial.springboot.rbac.entity.Resource;
import com.tutorial.springboot.rbac.repository.ResourceRepository;
import com.tutorial.springboot.rbac.transformer.ResourceTransformer;
import org.springframework.stereotype.Service;

@Service
public class ResourceService extends AbstractService<Long, Resource, ResourceDto> implements CrudService<Long, ResourceDto>, BatchService<Long, ResourceDto> {

    public ResourceService(ResourceRepository repository, ResourceTransformer transformer) {
        super(repository, transformer);
    }
}
