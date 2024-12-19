package com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestHttpUtils.TEST_USERNAME;

@Component
public class UserTestFactory extends AbstractTestFactory<Long, User, UserDto> {

    private int maxRoles = DEFAULT_MAX_RELATION;

    private int maxPermissions = DEFAULT_MAX_RELATION;

    @Autowired
    private RoleTestFactory roleFactory;

    public UserTestFactory(UserTransformer transformer) {
        super(transformer);
    }

    @Override
    protected UserDto newOne() {
        List<RoleDto> roles;
        var roleHelper = roleFactory.setUniqueRelations(uniqueRelations).newInstances(chooseRandom(maxPermissions)).dto();

        if (uniqueRelations) {
            roles = roleHelper.asUniqList(RoleDto::getName);
        } else {
            roles = roleHelper.asList();
        }

        return new UserDto()
                .setUsername(faker.name().fullName())
                .setPassword(faker.internet().password())
                .setEmail(faker.internet().emailAddress())
                .setEnabled(true)
                .setRoles(roles)
                .setCreatedBy(TEST_USERNAME)
                .setCreatedAt(LocalDateTime.now())
                .setVersion(0);
    }

    @Override
    protected Function<UserDto, ?> getComparator() {
        return UserDto::getUsername;
    }

    public int getMaxRoles() {
        return maxRoles;
    }

    public void setMaxRoles(int maxRoles) {
        this.maxRoles = maxRoles;
    }

    public int getMaxPermissions() {
        return maxPermissions;
    }

    public void setMaxPermissions(int maxPermissions) {
        this.maxPermissions = maxPermissions;
        roleFactory.setMaxPermissions(maxPermissions);
    }
}