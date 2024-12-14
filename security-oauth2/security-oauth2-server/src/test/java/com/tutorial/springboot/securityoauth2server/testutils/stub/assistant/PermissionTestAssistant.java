package com.tutorial.springboot.securityoauth2server.testutils.stub.assistant;

import com.tutorial.springboot.securityoauth2server.dto.PermissionDto;
import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.testutils.stub.ConversionHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.VarcharHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.factory.PermissionTestFactory;
import com.tutorial.springboot.securityoauth2server.transformer.PermissionTransformer;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.tutorial.springboot.securityoauth2server.testutils.SecurityTestUtils.loginToTestEnv;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class PermissionTestAssistant {

    @Autowired
    EntityManager entityManager;

    @Autowired
    PermissionTransformer transformer;

    @Autowired
    PermissionTestFactory factory;

    public ConversionHelper<Permission, PermissionDto> populate(int number) {
        var newPermissions = factory.newInstances(number).entity().asList();
        for (var permission : newPermissions) {
            entityManager.persist(permission);
        }
        entityManager.flush();
        entityManager.clear();

        var savedPermissions = entityManager.createQuery("SELECT p FROM Permission p", Permission.class).getResultList();

        var dtoList = transformer.toDtoList(savedPermissions);

        return new ConversionHelper<>(
                new VarcharHelper<>(savedPermissions.toArray(Permission[]::new)),
                new VarcharHelper<>(dtoList.toArray(PermissionDto[]::new))
        );
    }

    public ConversionHelper<Permission, PermissionDto> selectAllTest() {
        var entityArray = entityManager.createQuery("SELECT p FROM Permission p WHERE p.id < 1000", Permission.class)
                .getResultList()
                .toArray(Permission[]::new);

        var dtoArray = transformer.toDtoArray(entityArray);

        return new ConversionHelper<>(
                new VarcharHelper<>(entityArray),
                new VarcharHelper<>(dtoArray)
        );
    }
}
