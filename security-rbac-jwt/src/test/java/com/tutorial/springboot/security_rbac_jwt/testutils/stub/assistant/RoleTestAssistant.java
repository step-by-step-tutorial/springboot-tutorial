package com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant;

import com.tutorial.springboot.security_rbac_jwt.dto.RoleDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.service.impl.RoleService;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.RoleTestFactory;
import com.tutorial.springboot.security_rbac_jwt.transformer.RoleTransformer;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class RoleTestAssistant extends AbstractAssistant<Long, Role, RoleDto> {

    public RoleTestAssistant(RoleService service, RoleTransformer transformer, RoleTestFactory factory) {
        super(service, transformer, factory);
    }

    private int maxPermission = DEFAULT_MAX_RELATION;

    @Override
    protected void beforePopulation() {
        ((RoleTestFactory) factory).setMaxPermissions(maxPermission);
    }

    @Override
    protected Predicate<RoleDto> selectionFilter() {
        return dto -> dto.getId() < 1000;
    }

    public int getMaxPermission() {
        return maxPermission;
    }

    public void setMaxPermission(int maxPermission) {
        this.maxPermission = maxPermission;
    }

}
