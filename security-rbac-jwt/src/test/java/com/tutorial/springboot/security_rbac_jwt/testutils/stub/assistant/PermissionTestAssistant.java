package com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant;

import com.tutorial.springboot.security_rbac_jwt.dto.PermissionDto;
import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.service.impl.PermissionService;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.PermissionTestFactory;
import com.tutorial.springboot.security_rbac_jwt.transformer.PermissionTransformer;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class PermissionTestAssistant extends AbstractAssistant<Long, Permission, PermissionDto> {

    public PermissionTestAssistant(PermissionService service, PermissionTransformer transformer, PermissionTestFactory factory) {
        super(service, transformer, factory);
    }

    @Override
    protected Predicate<PermissionDto> selectionFilter() {
        return dto -> dto.getId() < 1000;
    }
}
