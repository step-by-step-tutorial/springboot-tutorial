package com.tutorial.springboot.securityoauth2server.testutils.stub.assistant;

import com.tutorial.springboot.securityoauth2server.dto.RoleDto;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.testutils.stub.ConversionHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.VarcharHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.factory.RoleTestFactory;
import com.tutorial.springboot.securityoauth2server.transformer.RoleTransformer;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class RoleTestAssistant {

    @Autowired
    EntityManager entityManager;

    @Autowired
    RoleTransformer transformer;

    @Autowired
    RoleTestFactory factory;

    private int maxPermission = 3;

    public ConversionHelper<Role, RoleDto> populate(int number) {
        factory.setMaxPermissions(maxPermission);
        var newRoles = factory.newInstances(number).entity().asList();
        for (var role : newRoles) {
            entityManager.persist(role);
        }
        entityManager.flush();
        entityManager.clear();

        var savedRoles = entityManager.createQuery("SELECT p FROM Role p", Role.class).getResultList();

        var dtoList = transformer.toDtoList(savedRoles);

        return new ConversionHelper<>(
                new VarcharHelper<>(savedRoles.toArray(Role[]::new)),
                new VarcharHelper<>(dtoList.toArray(RoleDto[]::new))
        );
    }

    public ConversionHelper<Role, RoleDto> selectAllTest() {
        var entityArray = entityManager.createQuery("SELECT p FROM Role p WHERE p.id < 1000", Role.class)
                .getResultList()
                .toArray(Role[]::new);

        var dtoArray = transformer.toDtoArray(entityArray);

        return new ConversionHelper<>(
                new VarcharHelper<>(entityArray),
                new VarcharHelper<>(dtoArray)
        );
    }

    public int getMaxPermission() {
        return maxPermission;
    }

    public void setMaxPermission(int maxPermission) {
        this.maxPermission = maxPermission;
    }
}
