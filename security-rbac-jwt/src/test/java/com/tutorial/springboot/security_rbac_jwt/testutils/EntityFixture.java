package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.entity.Permission;
import com.tutorial.springboot.security_rbac_jwt.entity.Role;
import com.tutorial.springboot.security_rbac_jwt.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Objects;
import java.util.stream.Stream;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestStringUtils.generateString;
import static java.util.stream.Collectors.toList;

public final class EntityFixture {

    private EntityFixture() {
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
        var role = EntityFixture.persistedGivenRole(em);
        transaction.commit();

        return role;
    }

    public static User newGivenUser() {
        return new User()
                .setUsername("username")
                .setPassword("password")
                .setEmail("username@email.com")
                .setEnabled(true)
                .setVersion(0);
    }

    public static User newGivenUser(String username) {
        Objects.requireNonNull(username);
        return new User()
                .setUsername(username)
                .setPassword("password")
                .setEmail(username + "@email.com")
                .setEnabled(true)
                .setVersion(0);
    }

    public static User newGivenUser(Role... roles) {
        return new User()
                .setUsername("username")
                .setPassword("password")
                .setEmail("username@email.com")
                .setEnabled(true)
                .setRoles(Stream.of(roles).collect(toList()))
                .setVersion(0);
    }

    public static User persistedGivenUser(EntityManager em) {
        var role = newGivenUser();
        em.persist(role);
        em.flush();
        em.clear();
        return role;
    }

    public static User persistedGivenUser(EntityManager em, Role... roles) {
        var user = newGivenUser(roles);
        em.persist(user);
        em.flush();
        em.clear();
        return user;
    }

    public static User persistedGivenUser(EntityManager em, String username) {
        var user = newGivenUser(username);
        em.persist(user);
        em.flush();
        em.clear();
        return user;
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

    public static class InvalidUsers implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new User()),
                    Arguments.of(new User().setUsername(null)),
                    Arguments.of(new User().setUsername("").setPassword("password").setEmail("username@email.com")),
                    Arguments.of(new User().setUsername("username").setPassword(null).setEmail("username@email.com")),
                    Arguments.of(new User().setUsername("username").setPassword("").setEmail("username@email.com")),
                    Arguments.of(new User().setUsername("username").setPassword("password").setEmail(null)),
                    Arguments.of(new User().setUsername("username").setPassword("password").setEmail(""))
            );
        }
    }
}
