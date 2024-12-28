package com.tutorial.springboot.security_rbac_jwt.testutils;

import com.tutorial.springboot.security_rbac_jwt.entity.User;
import jakarta.persistence.EntityManagerFactory;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_PASSWORD;
import static com.tutorial.springboot.security_rbac_jwt.testutils.TestConstant.TEST_USERNAME;
import static java.time.LocalDateTime.now;

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
        var auth = new UsernamePasswordAuthenticationToken(username, password);
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
