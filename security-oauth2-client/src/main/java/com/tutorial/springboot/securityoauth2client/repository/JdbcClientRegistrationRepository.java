package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.transformer.ClientRegistrationTransformer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Repository
public class JdbcClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {

    private final ClientRepository clientRepository;

    public JdbcClientRegistrationRepository(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ClientRegistration findByRegistrationId(String registrationId) {
        return clientRepository.findByRegistrationId(registrationId)
                .map(ClientRegistrationTransformer::toClientRegistration)
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Iterator<ClientRegistration> iterator() {
        return clientRepository.findAll()
                .stream()
                .map(ClientRegistrationTransformer::toClientRegistration)
                .iterator();
    }
}
