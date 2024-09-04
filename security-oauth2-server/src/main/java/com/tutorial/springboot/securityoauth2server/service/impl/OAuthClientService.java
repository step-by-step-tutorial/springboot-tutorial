package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.OAuthClientDto;
import com.tutorial.springboot.securityoauth2server.entity.OAuthClient;
import com.tutorial.springboot.securityoauth2server.repository.OAuthClientRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.OAuthClientTransformer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.tutorial.springboot.securityoauth2server.validation.ObjectValidation.isNotNullOrEmpty;

@Service
public class OAuthClientService extends AbstractService<Long, OAuthClient, OAuthClientDto> {

    private final PasswordEncoder passwordEncoder;

    public OAuthClientService(OAuthClientRepository repository, OAuthClientTransformer transformer, PasswordEncoder passwordEncoder) {
        super(repository, transformer);
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void beforeSave(OAuthClientDto dto, OAuthClient entity) {
        if (isNotNullOrEmpty(dto.getClientId())) {
            dto.setClientId(UUID.randomUUID().toString());
        }
        if (isNotNullOrEmpty(dto.getClientSecret())) {
            dto.setClientSecret(passwordEncoder.encode(UUID.randomUUID().toString()));
        }
    }
}
