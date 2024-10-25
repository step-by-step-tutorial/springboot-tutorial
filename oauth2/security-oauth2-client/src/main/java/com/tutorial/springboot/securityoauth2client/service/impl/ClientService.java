package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.repository.ClientRepository;
import com.tutorial.springboot.securityoauth2client.repository.ScopeRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.transformer.ClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends AbstractService<Long, Client, ClientDto> {

    @Autowired
    private ScopeRepository scopeRepository;

    public ClientService(ClientRepository repository, ClientTransformer transformer) {
        super(repository, transformer);
    }

    @Override
    protected void beforeSave(ClientDto dto, Client entity) {
        var scopes = entity.getScopes()
                .stream()
                .map(scope -> {
                    if (scopeRepository.existsByName(scope.getName())) {
                        return scopeRepository.findByName(scope.getName()).get();
                    } else {
                        return scope;
                    }
                })
                .toList();
        entity.setScopes(scopes);
    }
}
