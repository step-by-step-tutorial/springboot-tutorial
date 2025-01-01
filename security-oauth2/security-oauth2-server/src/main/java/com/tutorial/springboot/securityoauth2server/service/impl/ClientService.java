package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.AccessToken;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.repository.GrantTypeRepository;
import com.tutorial.springboot.securityoauth2server.repository.ScopeRepository;
import com.tutorial.springboot.securityoauth2server.repository.client.ClientRepository;
import com.tutorial.springboot.securityoauth2server.repository.rbac.UserRepository;
import com.tutorial.springboot.securityoauth2server.repository.token.AccessTokenRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.ClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;

@Service
public class ClientService extends AbstractService<Long, Client, ClientDto> {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScopeRepository scopeRepository;

    @Autowired
    private GrantTypeRepository grantTypeRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ClientService(ClientRepository repository, ClientTransformer transformer) {
        super(repository, transformer);
    }

    @Override
    protected void beforeSave(ClientDto dto, Client entity) {
        entity.setClientSecret(passwordEncoder.encode(dto.getClientSecret()));

        var scopes = scopeRepository.findOrSaveAll(entity.getScopes());
        entity.getScopes().clear();
        entity.getScopes().addAll(scopes);

        var grantTypes = grantTypeRepository.findOrSaveAll(entity.getGrantTypes());
        entity.getGrantTypes().clear();
        entity.getGrantTypes().addAll(grantTypes);
    }

    @Override
    protected void afterSave(ClientDto dto, Client entity) {
        updateToken(dto, entity);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasPermission(#dto, 'READ')")
    public Optional<ClientDto> getByClientId(String clientId) {
        return getClientRepository()
                .findByClientId(clientId)
                .map(transformer::toDto);
    }

    @PreAuthorize("hasPermission(#dto, 'UPDATE')")
    public void updateTokenBasedOnClientId(String clientId) {
        var clientEntity = getClientRepository().findByClientId(clientId);
        if (clientEntity.isEmpty()) {
            throw new RuntimeException("Client with clientId " + clientId + " not found");
        }
        var clientDto = clientEntity.map(transformer::toDto);
        if (clientDto.isEmpty()) {
            throw new RuntimeException("Client with clientId " + clientId + " not found");
        }

        updateToken(clientDto.get(), clientEntity.get());
    }

    private void updateToken(ClientDto dto, Client entity) {
        var jwtToken = tokenService.generateToken(getCurrentUsername(), dto).orElseThrow();
        var user = userRepository.findByUsername(getCurrentUsername()).orElseThrow();

        var accessToken = new AccessToken()
                .setToken(jwtToken.token().getBytes())
                .setExpiration(jwtToken.expiration())
                .setClient(entity)
                .setUser(user);

        accessTokenRepository.save(accessToken);
    }

    private ClientRepository getClientRepository() {
        return ClientRepository.class.cast(repository);
    }

}
