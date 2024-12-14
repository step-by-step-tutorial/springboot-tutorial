package com.tutorial.springboot.securityoauth2server.testutils.stub.factory;

import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.transformer.UserTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.tutorial.springboot.securityoauth2server.testutils.TestUtils.TEST_USERNAME;

@Component
public class UserTestFactory extends AbstractTestFactory<Long, User, UserDto> {

    private int maxRoles = 3;

    private int maxPermissions = 3;

    @Autowired
    private RoleTestFactory roleFactory;

    public UserTestFactory(UserTransformer transformer) {
        super(transformer);
    }

    @Override
    public UserDto newOne() {
        return new UserDto()
                .setUsername(faker.name().fullName())
                .setPassword(faker.internet().password())
                .setEmail(faker.internet().emailAddress())
                .setEnabled(true)
                .setRoles(roleFactory.newInstances(chooseRandom(maxRoles)).dto().asList())
                .setCreatedBy(TEST_USERNAME)
                .setCreatedAt(LocalDateTime.now())
                .setVersion(0);
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