package com.tutorial.springboot.securityoauth2server.fixture.role;

import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.tutorial.springboot.securityoauth2server.testutils.TestStringUtils.generateString;
import static java.util.stream.Collectors.toList;

public final class RoleEntityFixture {

    private RoleEntityFixture() {
    }

    public static Role newGivenRole() {
        return new Role()
                .setName("role")
                .setDescription("role description")
                .setVersion(0);
    }

    public static Role newGivenRole(Permission... permissions) {
        return new Role()
                .setName("role")
                .setDescription("role description")
                .setPermissions(Stream.of(permissions).collect(toList()))
                .setVersion(0);
    }

    public static Role newGivenRole(String name) {
        return new Role()
                .setName(name)
                .setDescription("role description")
                .setVersion(0);
    }

    public static Role newGivenRole(String name, Permission... permissions) {
        return new Role()
                .setName(name)
                .setDescription("role description")
                .setPermissions(Stream.of(permissions).collect(toList()))
                .setVersion(0);
    }

    public static Role persistedGivenRole(EntityManager em) {
        var role = newGivenRole();
        em.persist(role);
        em.flush();
        em.clear();
        return role;
    }

    public static Role persistedGivenRole(EntityManager em, Permission... permissions) {
        var role = newGivenRole(permissions);
        em.persist(role);
        em.flush();
        em.clear();
        return role;
    }

    public static Role persistedGivenRole(EntityManager em, String name) {
        var role = newGivenRole(name);
        em.persist(role);
        em.flush();
        em.clear();
        return role;
    }

    public static Role persistedGivenRole(EntityManagerFactory emf) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var role = RoleEntityFixture.persistedGivenRole(em);
        transaction.commit();

        return role;
    }

    public static Role findRoleById(EntityManagerFactory emf, Long id) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();
        transaction.begin();
        var role = em.find(Role.class, id);
        em.flush();
        em.clear();
        transaction.commit();
        return role;
    }

    public static class InvalidRoles implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Role()),
                    Arguments.of(new Role().setName(null)),
                    Arguments.of(new Role().setName("")),
                    Arguments.of(new Role().setName(generateString(51)))
            );
        }
    }
}
