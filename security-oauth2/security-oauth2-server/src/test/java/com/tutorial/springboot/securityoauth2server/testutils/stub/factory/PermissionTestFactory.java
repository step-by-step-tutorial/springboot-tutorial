package com.tutorial.springboot.securityoauth2server.testutils.stub.factory;

import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.testutils.TestUtils;
import com.tutorial.springboot.securityoauth2server.transformer.PermissionTransformer;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.TEST_USERNAME;

@Component
public class PermissionTestFactory extends AbstractTestFactory<Long, Permission, PermissionDto> {

    public PermissionTestFactory(PermissionTransformer transformer) {
        super(transformer);
    }

    @Override
    public PermissionDto newOne() {
        return new PermissionDto()
                .setName(faker.hacker().verb())
                .setCreatedBy(TEST_USERNAME)
                .setCreatedAt(LocalDateTime.now())
                .setVersion(0);
    }

}