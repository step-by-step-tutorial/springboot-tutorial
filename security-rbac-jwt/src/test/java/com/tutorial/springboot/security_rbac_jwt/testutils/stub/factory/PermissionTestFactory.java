package com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.transformer.PermissionTransformer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.TEST_USERNAME;

@Component
public class PermissionTestFactory extends AbstractTestFactory<Long, Permission, PermissionDto> {

    public PermissionTestFactory(PermissionTransformer transformer) {
        super(transformer);
    }

    @Override
    protected PermissionDto newOne() {
        return new PermissionDto()
                .setName(faker.hacker().verb())
                .setCreatedBy(TEST_USERNAME)
                .setCreatedAt(LocalDateTime.now())
                .setVersion(0);
    }

}