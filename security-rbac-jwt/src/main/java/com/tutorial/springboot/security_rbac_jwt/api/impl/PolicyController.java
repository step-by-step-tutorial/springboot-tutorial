package com.tutorial.springboot.security_rbac_jwt.api.impl;

import com.tutorial.springboot.security_rbac_jwt.api.AllApi;
import com.tutorial.springboot.security_rbac_jwt.dto.PolicyDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Policy;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PolicyService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/policies")
public class PolicyController extends AllApi<Long, Policy, PolicyDto> {

    public PolicyController(PolicyService service) {
        super(service);
    }

    @Override
    protected PolicyService getService() {
        return (PolicyService) service;
    }
}
