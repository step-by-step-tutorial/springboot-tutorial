package com.tutorial.springboot.securitydynamicpolicy.service.impl;

import com.tutorial.springboot.securitydynamicpolicy.dto.PolicyDto;
import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import com.tutorial.springboot.securitydynamicpolicy.repository.PolicyRepository;
import com.tutorial.springboot.securitydynamicpolicy.service.AbstractService;
import com.tutorial.springboot.securitydynamicpolicy.service.BatchService;
import com.tutorial.springboot.securitydynamicpolicy.service.CrudService;
import com.tutorial.springboot.securitydynamicpolicy.transformer.PolicyTransformer;
import org.springframework.stereotype.Service;

@Service
public class PolicyService extends AbstractService<Long, Policy, PolicyDto> implements CrudService<Long, PolicyDto>, BatchService<Long, PolicyDto> {

    public PolicyService(PolicyRepository repository, PolicyTransformer transformer) {
        super(repository, transformer);
    }
}
