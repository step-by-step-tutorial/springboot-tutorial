package com.tutorial.springboot.securityoauth2client.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class ClientConfig {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(buildTestClientRegistration());
    }

    private ClientRegistration buildTestClientRegistration() {
        return ClientRegistration.withRegistrationId("test-client")
                .clientId("test-client")
                .clientSecret("test-secret")
                .scope("read")
                .authorizationUri("http://localhost:8080/oauth2/authorize")
                .tokenUri("http://localhost:8080/oauth2/token")
                .redirectUri("http://localhost:9000/login/oauth2/code/test-client")
                .userInfoUri("http://localhost:8080/userinfo")
                .clientName("Test Client")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .build();
    }

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults())
                .csrf(config -> config.disable());
        return http.build();
    }

}
