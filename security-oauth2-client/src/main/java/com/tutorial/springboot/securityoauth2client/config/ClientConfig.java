package com.tutorial.springboot.securityoauth2client.config;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.dto.OauthProperties;
import com.tutorial.springboot.securityoauth2client.service.impl.ClientService;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static com.tutorial.springboot.securityoauth2client.util.ClientInitializerFactory.createTestClient;
import static org.slf4j.LoggerFactory.getLogger;

@Configuration
public class ClientConfig {

    private final Logger logger = getLogger(this.getClass());

    private final ClientService clientService;

    private final OauthProperties oauthProperties;

    public ClientConfig(ClientService clientService, OauthProperties oauthProperties) {
        this.clientService = clientService;
        this.oauthProperties = oauthProperties;
    }

    @Bean
    public ClientDto initClient() {
        logger.info("Initializing static users");
        var client = createTestClient(oauthProperties.serverUrl(), oauthProperties.clientUrl());
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
