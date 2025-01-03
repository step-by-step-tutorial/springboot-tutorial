package com.tutorial.springboot.securitydynamicpolicy.api.impl;

import com.tutorial.springboot.securitydynamicpolicy.api.AllApi;
import com.tutorial.springboot.securitydynamicpolicy.dto.PolicyDto;
import com.tutorial.springboot.securitydynamicpolicy.entity.Policy;
import com.tutorial.springboot.securitydynamicpolicy.service.impl.PolicyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/policies")
public class PolicyController extends AllApi<Long, Policy, PolicyDto> {

    public PolicyController(PolicyService service) {
        super(service);
    }

    @Override
    protected PolicyService getService() {
        return (PolicyService) service;
    }
}
