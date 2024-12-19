package com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant;

import com.tutorial.springboot.security_rbac_jwt.dto.UserDto;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import com.tutorial.springboot.security_rbac_jwt.service.impl.UserService;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.ConversionHelper;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.VarcharHelper;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.UserTestFactory;
import com.tutorial.springboot.security_rbac_jwt.transformer.UserTransformer;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils.login;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class UserTestAssistant extends AbstractAssistant<Long, User, UserDto> {

    private int maxRoles = DEFAULT_MAX_RELATION;

    private int maxPermission = DEFAULT_MAX_RELATION;

    public UserTestAssistant(UserService service, UserTransformer transformer, UserTestFactory factory) {
        super(service, transformer, factory);
    }

    @Override
    protected void beforePopulation() {
        ((UserTestFactory) factory).setMaxPermissions(maxPermission);
        ((UserTestFactory) factory).setMaxRoles(maxRoles);
    }

    @Override
    protected Predicate<UserDto> selectionFilter() {
        return dto -> dto.getId() < 1000;
    }

    public int getMaxRoles() {
        return maxRoles;
    }

    public void setMaxRoles(int maxRoles) {
        this.maxRoles = maxRoles;
    }

    public int getMaxPermission() {
        return maxPermission;
    }

    public void setMaxPermission(int maxPermission) {
        this.maxPermission = maxPermission;
    }

    public ConversionHelper<User, UserDto> signupAndLogin() {
        var entity = populate(1).entity().asOne();
        login(entity.getUsername(), entity.getPassword());
        var dto = transformer.toDto(entity);
        return new ConversionHelper<>(new VarcharHelper<>(entity), new VarcharHelper<>(dto));
    }
}
