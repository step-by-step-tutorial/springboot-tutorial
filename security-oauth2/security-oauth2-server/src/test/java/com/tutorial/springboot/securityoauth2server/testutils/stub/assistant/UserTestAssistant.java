package com.tutorial.springboot.securityoauth2server.testutils.stub.assistant;

import com.tutorial.springboot.securityoauth2server.dto.UserDto;
import com.tutorial.springboot.securityoauth2server.entity.User;
import com.tutorial.springboot.securityoauth2server.testutils.stub.ConversionHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.VarcharHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.factory.UserTestFactory;
import com.tutorial.springboot.securityoauth2server.transformer.UserTransformer;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class UserTestAssistant {

    @Autowired
    EntityManager entityManager;

    @Autowired
    UserTransformer transformer;

    @Autowired
    UserTestFactory factory;

    private int maxRoles = 3;

    private int maxPermission = 3;

    public ConversionHelper<User, UserDto> populate(int number) {
        factory.setMaxRoles(maxRoles);
        factory.setMaxPermissions(maxPermission);
        var newUsers = factory.newInstances(number).entity().asList();
        for (var user : newUsers) {
            entityManager.persist(user);
        }
        entityManager.flush();
        entityManager.clear();

        var savedUsers = entityManager.createQuery("SELECT p FROM User p", User.class).getResultList();

        var dtoList = transformer.toDtoList(savedUsers);

        return new ConversionHelper<>(
                new VarcharHelper<>(savedUsers.toArray(User[]::new)),
                new VarcharHelper<>(dtoList.toArray(UserDto[]::new))
        );
    }

    public ConversionHelper<User, UserDto> selectAllTest() {
        var entityArray = entityManager.createQuery("SELECT p FROM User p WHERE p.id < 1000", User.class)
                .getResultList()
                .toArray(User[]::new);

        var dtoArray = transformer.toDtoArray(entityArray);

        return new ConversionHelper<>(
                new VarcharHelper<>(entityArray),
                new VarcharHelper<>(dtoArray)
        );
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
}
