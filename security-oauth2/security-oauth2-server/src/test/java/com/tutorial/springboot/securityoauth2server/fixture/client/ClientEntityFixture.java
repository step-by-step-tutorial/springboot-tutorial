package com.tutorial.springboot.securityoauth2server.fixture.client;

import com.tutorial.springboot.securityoauth2server.entity.*;
import com.tutorial.springboot.securityoauth2server.enums.GrantTypeEnum;
import com.tutorial.springboot.securityoauth2server.enums.ScopeEnum;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import net.datafaker.Faker;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.time.LocalDateTime;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public final class ClientEntityFixture {

    private static final Faker faker = new Faker();

    private ClientEntityFixture() {
    }

    public static Client newGivenClient() {
        var name = faker.app().name().replace(" ", "_");
        return new Client()
                .setClientId(name)
                .setClientSecret("password")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/" + name)
                .setGrantTypes(GrantTypeEnum.allType().stream().map(it -> new GrantType().setName(it)).collect(toList()))
                .setScopes(ScopeEnum.allType().stream().map(it -> new Scope().setName(it)).collect(toList()))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600)
                .setVersion(0);
    }

    public static Client newGivenClient(String name) {
        return new Client()
                .setClientId(name)
                .setClientSecret("password")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/" + name)
                .setGrantTypes(GrantTypeEnum.allType().stream().map(it -> new GrantType().setName(it)).collect(toList()))
                .setScopes(ScopeEnum.allType().stream().map(it -> new Scope().setName(it)).collect(toList()))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600)
                .setVersion(0);
    }

    public static Client persistedGivenClient(EntityManager em) {
        var entity = newGivenClient();
        em.persist(entity);
        em.flush();
        var user = em.find(User.class, 3000L);
        var accessToken = new AccessToken()
                .setClient(entity)
                .setToken("eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImdyYW50LXR5cGVzIjoiQVVUSE9SSVpBVElPTl9DT0RFIElNUExJQ0lUIFBBU1NXT1JEIENMSUVOVF9DUkVERU5USUFMUyBSRUZSRVNIX1RPS0VOIEpXVF9CRUFSRVIiLCJpc3MiOiJzZWxmIiwic2NvcGVzIjoicmVhZCB3cml0ZSIsImV4cCI6MTczNTcxMjc0OCwiY2xpZW50LWlkIjoiWmF0aGluIiwiaWF0IjoxNzM1Njc2NzQ4fQ.OrSuKhVipcSmwLJPcPmegGJVvAI6DeDn3qMuS-1tHuTh3AtVnDwmyel4RKrHxcPi2sJNfItZ_rt_TLxLRkS_Wj-1fpHLydOFw2Eik4_bpCypM2vYx7xExVpsaAtHS46VJHkKTATT3U4ZD1VVHk-_BmEfHwaC15I7RRMXqVhCf-W3cjtkAXenRoxwFYwW4FvkEBTdEJcpXYiqmfitdYhupF-BZ05xehx6tGv_Tcsmxl9f4ODMzzbId1dKdEoNupEaEe2PKiiXNo_GG3TryT92qHTCVVl5PdYDKnrCJyyUj4hQpakDpOnb7tdGn17BXQXaHqHKWZuVWATz9UxtwxJ7Dw".getBytes())
                .setExpiration(LocalDateTime.parse("2025-01-01T07:25:48.346935200"))
                .setUser(user)
                .setVersion(0);
        em.persist(accessToken);
        em.flush();
        em.clear();
        return entity;
    }

    public static Client persistedGivenClient(EntityManager em, String name) {
        var entity = newGivenClient(name);
        em.persist(entity);
        em.flush();
        var user = em.find(User.class, 3000L);
        var accessToken = new AccessToken()
                .setClient(entity)
                .setToken("eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ0ZXN0Iiwicm9sZXMiOlsiQURNSU4iLCJVU0VSIl0sImdyYW50LXR5cGVzIjoiQVVUSE9SSVpBVElPTl9DT0RFIElNUExJQ0lUIFBBU1NXT1JEIENMSUVOVF9DUkVERU5USUFMUyBSRUZSRVNIX1RPS0VOIEpXVF9CRUFSRVIiLCJpc3MiOiJzZWxmIiwic2NvcGVzIjoicmVhZCB3cml0ZSIsImV4cCI6MTczNTcxMjc0OCwiY2xpZW50LWlkIjoiWmF0aGluIiwiaWF0IjoxNzM1Njc2NzQ4fQ.OrSuKhVipcSmwLJPcPmegGJVvAI6DeDn3qMuS-1tHuTh3AtVnDwmyel4RKrHxcPi2sJNfItZ_rt_TLxLRkS_Wj-1fpHLydOFw2Eik4_bpCypM2vYx7xExVpsaAtHS46VJHkKTATT3U4ZD1VVHk-_BmEfHwaC15I7RRMXqVhCf-W3cjtkAXenRoxwFYwW4FvkEBTdEJcpXYiqmfitdYhupF-BZ05xehx6tGv_Tcsmxl9f4ODMzzbId1dKdEoNupEaEe2PKiiXNo_GG3TryT92qHTCVVl5PdYDKnrCJyyUj4hQpakDpOnb7tdGn17BXQXaHqHKWZuVWATz9UxtwxJ7Dw".getBytes())
                .setExpiration(LocalDateTime.parse("2025-01-01T07:25:48.346935200"))
                .setUser(user)
                .setVersion(0);
        em.persist(accessToken);
        em.flush();
        em.clear();
        return entity;
    }

    public static Client persistedGivenClient(EntityManagerFactory emf) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var permission = persistedGivenClient(em);
        transaction.commit();

        return permission;
    }

    public static Client persistedGivenClient(EntityManagerFactory emf, String name) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var permission = persistedGivenClient(em, name);
        transaction.commit();

        return permission;
    }

    public static Client findClientById(EntityManagerFactory emf, Long id) {
        var em = emf.createEntityManager();
        var transaction = em.getTransaction();
        transaction.begin();
        var client = em.find(Client.class, id);
        em.flush();
        em.clear();
        transaction.commit();
        return client;
    }

    public static class InvalidClients implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(new Client())
            );
        }
    }

}
