package com.tutorial.springboot.securityoauth2client.config;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.service.impl.ClientService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class ClientConfig {

//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository() {
//        return new InMemoryClientRegistrationRepository(buildTestClientRegistration());
//    }
//
//    private ClientRegistration buildTestClientRegistration() {
//        return ClientRegistration.withRegistrationId("testClient")
//                .clientId("testClient")
//                .clientName("Test Client")
//                .clientSecret("test-secret")
//                .scope("read")
//                .authorizationUri("http://auth-server.local:8080/oauth2/authorize")
//                .tokenUri("http://auth-server.local:8080/oauth2/token")
//                .redirectUri("http://client-app.local:9000/login/oauth2/code/testClient")
//                .userInfoUri("http://auth-server.local:8080/api/v1/users/userinfo")
//                .userNameAttributeName("sub")
//                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
//                .build();
//    }

    private final Logger logger = getLogger(this.getClass());

    private final ClientService clientService;

    public ClientConfig(ClientService clientService) {
        this.clientService = clientService;
    }

    @Bean
    public ClientDto initClient() {
        logger.info("Initializing static users");
        var client = new ClientDto().setRegistrationId("testClient")
                .setClientId("testClient")
                .setClientName("Test Client")
                .setClientSecret("test-secret")
                .setAuthorizationUri("http://auth-server.local:8080/oauth2/authorize")
                .setScope("read")
                .setAuthorizationGrantType("authorization_code")
                .setTokenUri("http://auth-server.local:8080/oauth2/token")
                .setUserNameAttributeName("sub")
                .setUri("http://auth-server.local:8080/api/v1/users/userinfo")
                .setRedirectUri("http://client-app.local:9000/login/oauth2/code/testClient");
        clientService.save(client);
        return client;
    }

    @Bean
    public SecurityFilterChain clientFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .oauth2Login(Customizer.withDefaults())
                .oauth2Client(Customizer.withDefaults()).build();
    }

}
