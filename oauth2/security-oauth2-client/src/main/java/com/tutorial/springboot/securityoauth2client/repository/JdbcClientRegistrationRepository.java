package com.tutorial.springboot.securityoauth2client.repository;

import com.tutorial.springboot.securityoauth2client.entity.Scope;
import com.tutorial.springboot.securityoauth2client.transformer.ClientRegistrationTransformer;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;

import static com.tutorial.springboot.securityoauth2client.transformer.ClientRegistrationTransformer.toClientEntity;

@Repository
public class JdbcClientRegistrationRepository implements ClientRegistrationRepository, Iterable<ClientRegistration> {

    private final ClientRepository clientRepository;

    private final ScopeRepository scopeRepository;

    public JdbcClientRegistrationRepository(ClientRepository clientRepository, ScopeRepository scopeRepository) {
        this.clientRepository = clientRepository;
        this.scopeRepository = scopeRepository;
    }

    @Transactional
    public void save(ClientRegistration clientRegistration) {
        var scopes = clientRegistration.getScopes()
                .stream()
                .map(name -> scopeRepository.findByName(name).orElseGet(() -> new Scope().setName(name)))
                .toList();

        clientRepository.save(toClientEntity(clientRegistration).setScopes(scopes));
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

    public Boolean existsByRegistrationId(String registrationId) {
        return clientRepository.existsByRegistrationId(registrationId);
    }

}
