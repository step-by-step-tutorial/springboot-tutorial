package com.tutorial.springboot.securityoauth2server.service.impl;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.AccessToken;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.repository.AccessTokenRepository;
import com.tutorial.springboot.securityoauth2server.repository.ClientRepository;
import com.tutorial.springboot.securityoauth2server.repository.UserRepository;
import com.tutorial.springboot.securityoauth2server.service.AbstractService;
import com.tutorial.springboot.securityoauth2server.transformer.ClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;

@Service
public class ClientService extends AbstractService<Long, Client, ClientDto> {

    @Autowired
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private UserRepository userRepository;

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
