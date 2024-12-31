package com.tutorial.springboot.securityoauth2server.testutils;

import com.tutorial.springboot.securityoauth2server.entity.Permission;
import com.tutorial.springboot.securityoauth2server.entity.Role;
import com.tutorial.springboot.securityoauth2server.entity.User;
import jakarta.persistence.EntityManagerFactory;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.tutorial.springboot.securityoauth2server.testutils.TestConstant.TEST_PASSWORD;
import static com.tutorial.springboot.securityoauth2server.testutils.TestConstant.TEST_USERNAME;

@Component
public class TestAuthenticationHelper {

    private static final Faker FAKER = new Faker();

    @Autowired
    private EntityManagerFactory emf;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public TestAuthenticationHelper() {
    }

    public static void login() {
        login(TEST_USERNAME, TEST_PASSWORD);
    }

    public static void login(String username, String password) {
        var creatPermission = new Permission().setName("CREAT");
        var readPermission = new Permission().setName("READ");
        var updatePermission = new Permission().setName("UPDATE");
        var deletePermission = new Permission().setName("DELETE");
        var adminRole = new Role().setName("ADMIN").setPermissions(List.of(creatPermission, readPermission, updatePermission, deletePermission));
        var userRole = new Role().setName("USER").setPermissions(List.of(readPermission));
        var user = new User().setUsername(username).setPassword(password).setRoles(List.of(adminRole, userRole));

        var auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.setContext(new SecurityContextImpl(auth));
    }

    public User signupAndLogin() {
        var username = FAKER.internet().username();
        var email = FAKER.internet().emailAddress();
        var password = FAKER.internet().password();
        var encodedPassword = passwordEncoder.encode(password);

        var em = emf.createEntityManager();
        var transaction = em.getTransaction();

        transaction.begin();
        var user = new User()
                .setUsername(username)
                .setPassword(encodedPassword)
                .setEmail(email)
                .setEnabled(true);

        em.persist(user);
        em.flush();
        em.clear();
        transaction.commit();

        login(user.getUsername(), password);

        return user.setPassword(password);
    }
}
