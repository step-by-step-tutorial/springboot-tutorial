package com.tutorial.springboot.security_rbac_jwt.service.impl;

import com.tutorial.springboot.security_rbac_jwt.dto.PolicyDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Policy;
import com.tutorial.springboot.security_rbac_jwt.repository.PolicyRepository;
import com.tutorial.springboot.security_rbac_jwt.service.AbstractService;
import com.tutorial.springboot.security_rbac_jwt.service.BatchService;
import com.tutorial.springboot.security_rbac_jwt.service.CrudService;
import com.tutorial.springboot.security_rbac_jwt.transformer.PolicyTransformer;
import org.springframework.stereotype.Service;

@Service
public class PolicyService extends AbstractService<Long, Policy, PolicyDto> implements CrudService<Long, PolicyDto>, BatchService<Long, PolicyDto> {

    public PolicyService(PolicyRepository repository, PolicyTransformer transformer) {
        super(repository, transformer);
    }
}
