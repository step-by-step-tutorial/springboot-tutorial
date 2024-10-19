package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.repository.ClientRepository;
import com.tutorial.springboot.securityoauth2client.transformer.RegisteredClientTransformer;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

import static com.tutorial.springboot.securityoauth2client.transformer.RegisteredClientTransformer.toClient;

@Service
public class RegisteredClientService implements RegisteredClientRepository {

    private final ClientRepository clientRepository;

    public RegisteredClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {
        clientRepository.save(toClient(registeredClient));
    }

    @Override
    public RegisteredClient findById(String id) {
        return clientRepository.findById(Long.parseLong(id))
                .map(RegisteredClientTransformer::toRegisteredClient).orElseThrow();
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return clientRepository.findByClientId(clientId)
                .map(RegisteredClientTransformer::toRegisteredClient)
                .orElseThrow(() -> new NoSuchElementException("Client with clientId " + clientId + " not found"));
    }


}
