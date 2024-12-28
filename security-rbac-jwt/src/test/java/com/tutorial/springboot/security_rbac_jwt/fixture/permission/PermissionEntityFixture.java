package com.tutorial.springboot.security_rbac_jwt.fixture.permission;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestStringUtils.generateString;

public final class PermissionEntityFixture {

    private PermissionEntityFixture() {
    }

    public static Permission newGivenPermission() {
        return new Permission()
                .setName("permission")
                .setDescription("permission description")
                .setVersion(0);
    }

    public static Permission newGivenPermission(String name) {
        return new Permission()
                .setName(name)
                .setDescription("permission description")
                .setVersion(0);
    }

    public static Permission persistedGivenPermission(EntityManager em) {
        var entity = newGivenPermission();
        em.persist(entity);
        em.flush();
        em.clear();
        return entity;
    }

    public static Permission persistedGivenPermission(EntityManager em, String name) {
        var entity = newGivenPermission(name);
        em.persist(entity);
        em.flush();
        em.clear();
        return entity;
    }

    public static Permission persistedGivenPermission(EntityManagerFactory emf) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var permission = persistedGivenPermission(em);
        transaction.commit();

        return permission;
    }

    public static Permission findPermissionById(EntityManagerFactory emf, Long id) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();
        transaction.begin();
        var permission = em.find(Permission.class, id);
        em.flush();
        em.clear();
        transaction.commit();
        return permission;
    }

    public static class InvalidPermissions implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Permission()),
                    Arguments.of(new Permission().setName(null)),
                    Arguments.of(new Permission().setName("")),
                    Arguments.of(new Permission().setName(generateString(51)))
            );
        }
    }

}
