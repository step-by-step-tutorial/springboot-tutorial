package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.AccessToken;
import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.entity.Scope;
import com.tutorial.springboot.securityoauth2client.repository.AccessTokenRepository;
import com.tutorial.springboot.securityoauth2client.repository.ClientRepository;
import com.tutorial.springboot.securityoauth2client.repository.ScopeRepository;
import com.tutorial.springboot.securityoauth2client.repository.UserRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.transformer.ClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.tutorial.springboot.securityoauth2client.util.SecurityUtils.getCurrentUsername;

@Service
public class ClientService extends AbstractService<Long, Client, ClientDto> {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ScopeRepository scopeRepository;

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

    @Override
    protected void afterSave(ClientDto dto, Client entity) {
        var jwtToken = tokenService.generateToken(getCurrentUsername(), dto).orElseThrow();
        var user = userRepository.findByUsername(getCurrentUsername()).orElseThrow();

        var accessToken = new AccessToken()
                .setToken(jwtToken.token().getBytes())
                .setExpiration(jwtToken.expiration())
                .setClient(entity)
                .setUser(user);

        accessTokenRepository.save(accessToken);
    }

    public Optional<ClientDto> getByClientId(String clientId) {
        return (ClientRepository.class.cast(repository)).findByClientId(clientId).map(transformer::toDto);
    }

}
