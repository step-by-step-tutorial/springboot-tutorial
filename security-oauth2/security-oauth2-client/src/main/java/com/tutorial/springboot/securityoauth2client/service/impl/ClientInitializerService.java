package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.repository.JdbcClientRegistrationRepository;
import com.tutorial.springboot.securityoauth2client.util.ClientRegistrationFactory;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import static org.slf4j.LoggerFactory.getLogger;

@Service
public class ClientInitializerService {

    private final Logger logger = getLogger(this.getClass());

    private final JdbcClientRegistrationRepository clientRegistrationRepository;

    private final ClientRegistrationFactory clientRegistrationFactory;

    public ClientInitializerService(JdbcClientRegistrationRepository clientRegistrationRepository, ClientRegistrationFactory clientRegistrationFactory) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.clientRegistrationFactory = clientRegistrationFactory;
        initClient();
    }

    public void initClient() {
        logger.info("Initializing static clients");
        var adminClientExists = clientRegistrationRepository.existsByRegistrationId("adminClient");
        if (!adminClientExists) {
            clientRegistrationRepository.save(clientRegistrationFactory.createAdminClient());
        }

        var testClientExists = clientRegistrationRepository.existsByRegistrationId("testClient");
        if (!testClientExists) {
            clientRegistrationRepository.save(clientRegistrationFactory.createTestClient());
        }

    }

}
