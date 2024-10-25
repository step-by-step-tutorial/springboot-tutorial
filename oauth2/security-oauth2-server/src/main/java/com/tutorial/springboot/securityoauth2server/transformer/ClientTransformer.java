package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.dto.TokenDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.stereotype.Component;

@Component
public class ClientTransformer extends AbstractTransformer<Long, Client, ClientDto> {

    @Override
    protected void copyEntityToDto(Client entity, ClientDto dto) {
        var token = entity.getAccessTokens().getFirst();
        dto.setClientId(entity.getClientId())
                .setClientSecret(entity.getClientSecret())
                .setRedirectUri(entity.getRedirectUri())
                .setGrantTypes(entity.getGrantTypes().stream().toList())
                .setScopes(entity.getScopes()
                        .stream()
                        .map(Scope::getName)
                        .toList())
                .setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds())
                .setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds())
                .setToken(new TokenDto(new String(token.getToken()), token.getExpiration()));
    }

    @Override
    protected void copyDtoToEntity(ClientDto dto, Client entity) {
        entity.setClientId(dto.getClientId())
                .setClientSecret(dto.getClientSecret())
                .setRedirectUri(dto.getRedirectUri())
                .setGrantTypes(dto.getGrantTypes().stream().toList())
                .setScopes(dto.getScopes()
                        .stream()
                        .map(s -> new Scope().setName(s))
                        .toList())
                .setAccessTokenValiditySeconds(dto.getAccessTokenValiditySeconds())
                .setRefreshTokenValiditySeconds(dto.getRefreshTokenValiditySeconds());
    }
}
