package com.tutorial.springboot.securityoauth2server.repository.client;

import com.tutorial.springboot.securityoauth2server.transformer.RegisteredClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

@Repository
@Transactional
public class CustomRegisteredClientRepository implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    private final RegisteredClientTransformer registeredClientTransformer;

    public CustomRegisteredClientRepository(ClientRepository clientRepository, RegisteredClientTransformer registeredClientTransformer) {
        this.clientRepository = clientRepository;
        this.registeredClientTransformer = registeredClientTransformer;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(registeredClientTransformer.toClient(registeredClient));
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findById(String id) {
        return clientRepository.findById(Long.parseLong(id))
                .map(registeredClientTransformer::toRegisteredClient)
                .orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .map(registeredClientTransformer::toRegisteredClient)
                .orElseThrow(() -> new NoSuchElementException("Client with clientId " + clientId + " not found"));
    }


}
