package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.repository.ClientRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.ClientTransformer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tutorial.springboot.securityoauth2server.validation.ObjectValidation.isNotNullOrEmpty;

@Service
public class ClientService extends AbstractService<Long, Client, ClientDto> {

    private final PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository repository, ClientTransformer transformer, PasswordEncoder passwordEncoder) {
        super(repository, transformer);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void beforeSave(ClientDto dto, Client entity) {
        if (isNotNullOrEmpty(dto.getClientId())) {
            dto.setClientId(UUID.randomUUID().toString());
        }
        if (isNotNullOrEmpty(dto.getClientSecret())) {
            dto.setClientSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
        }
    }
}
