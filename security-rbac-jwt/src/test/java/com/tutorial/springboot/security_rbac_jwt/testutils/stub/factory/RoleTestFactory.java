package com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.transformer.RoleTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_USERNAME;

@Component
public class RoleTestFactory extends AbstractTestFactory<Long, Role, RoleDto> {

    private int maxPermissions = DEFAULT_MAX_RELATION;

    @Autowired
    private PermissionTestFactory permissionFactory;

    public RoleTestFactory(RoleTransformer transformer) {
        super(transformer);
    }

    @Override
    protected RoleDto newOne() {
        List<PermissionDto> permissions;
        var permissionHelper = permissionFactory.newInstances(chooseRandom(maxPermissions)).dto();

        if (uniqueRelations) {
            permissions = permissionHelper.asUniqList(PermissionDto::getName);
        } else {
            permissions = permissionHelper.asList();
        }

        return new RoleDto()
                .setName(faker.job().title())
                .setPermissions(permissions)
                .setCreatedBy(TEST_USERNAME)
                .setCreatedAt(LocalDateTime.now())
                .setVersion(0);

    }

    @Override
    protected Function<RoleDto, ?> getComparator() {
        return RoleDto::getName;
    }

    public int getMaxPermissions() {
        return maxPermissions;
    }

    public void setMaxPermissions(int maxPermissions) {
        this.maxPermissions = maxPermissions;
    }
}