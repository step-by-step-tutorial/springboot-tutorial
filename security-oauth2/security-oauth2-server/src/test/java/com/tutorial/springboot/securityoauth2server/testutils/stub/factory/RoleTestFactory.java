package com.tutorial.springboot.securityoauth2server.testutils.stub.factory;

import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.transformer.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.tutorial.springboot.securityoauth2server.testutils.TestHttpUtils.TEST_USERNAME;

@Component
public class RoleTestFactory extends AbstractTestFactory<Long, Role, RoleDto> {

    private int maxPermissions = 3;

    @Autowired
    private PermissionTestFactory permissionFactory;

    public RoleTestFactory(RoleTransformer transformer) {
        super(transformer);
    }

    @Override
    public RoleDto newOne() {
        return new RoleDto()
                .setName(faker.job().title())
                .setPermissions(permissionFactory.newInstances(chooseRandom(maxPermissions)).dto().asList())
                .setCreatedBy(TEST_USERNAME)
                .setCreatedAt(LocalDateTime.now())
                .setVersion(0);
    }

    public int getMaxPermissions() {
        return maxPermissions;
    }

    public void setMaxPermissions(int maxPermissions) {
        this.maxPermissions = maxPermissions;
    }
}