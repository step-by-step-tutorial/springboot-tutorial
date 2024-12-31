package com.tutorial.springboot.securityoauth2server.fixture.user;

import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.Objects;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class UserEntityFixture {

    private UserEntityFixture() {
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

    public static User persistedGivenUser(EntityManagerFactory emf) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var user = UserEntityFixture.newGivenUser();
        em.persist(user);
        em.flush();
        em.clear();
        transaction.commit();

        return user;
    }

    public static User findUserById(EntityManagerFactory emf, Long id) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();
        transaction.begin();
        var user = em.find(User.class, id);
        em.flush();
        em.clear();
        transaction.commit();
        return user;
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
