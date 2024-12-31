package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.dto.TokenDto;
import com.tutorial.springboot.securityoauth2server.entity.AccessToken;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.stream.Collectors;

@Component
public class ClientTransformer extends AbstractTransformer<Long, Client, ClientDto> {

    @Override
    protected void copyEntityToDto(Client entity, ClientDto dto) {
        dto.setClientId(entity.getClientId())
                .setRedirectUri(entity.getRedirectUri())
                .setGrantTypes(entity.getGrantTypes())
                .setScopes(entity.getScopes()
                        .stream()
                        .map(Scope::getName)
                        .collect(Collectors.toList()))
                .setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds())
                .setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());

        getLastToken(entity, dto);
    }

    @Override
    protected void copyDtoToEntity(ClientDto dto, Client entity) {
        entity.setClientId(dto.getClientId())
                .setClientSecret(dto.getClientSecret())
                .setRedirectUri(dto.getRedirectUri())
                .setGrantTypes(dto.getGrantTypes())
                .setScopes(dto.getScopes()
                        .stream()
                        .map(s -> new Scope().setName(s))
                        .collect(Collectors.toList()))
                .setAccessTokenValiditySeconds(dto.getAccessTokenValiditySeconds())
                .setRefreshTokenValiditySeconds(dto.getRefreshTokenValiditySeconds());
    }

    private void getLastToken(Client entity, ClientDto dto) {
        entity.getAccessTokens()
                .stream()
                .max(Comparator.comparing(AccessToken::getExpiration))
                .ifPresent(it -> dto.setToken(new TokenDto(new String(it.getToken()), it.getExpiration())));
    }
}
