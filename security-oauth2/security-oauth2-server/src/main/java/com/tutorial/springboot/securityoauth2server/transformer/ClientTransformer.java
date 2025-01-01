package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.dto.TokenDto;
import com.tutorial.springboot.securityoauth2server.entity.AccessToken;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Comparator;

@Component
public class ClientTransformer extends AbstractTransformer<Long, Client, ClientDto> {

    @Autowired
    private ScopeTransformer scopeTransformer;

    @Autowired
    private GrantTypeTransformer grantTypeTransformer;

    @Override
    protected void copyEntityToDto(Client entity, ClientDto dto) {
        dto.setClientId(entity.getClientId())
                .setRedirectUri(entity.getRedirectUri())
                .setGrantTypes(grantTypeTransformer.toStringList(entity.getGrantTypes()))
                .setScopes(scopeTransformer.toStringList(entity.getScopes()))
                .setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds())
                .setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());

        getLastToken(entity, dto);
    }

    @Override
    protected void copyDtoToEntity(ClientDto dto, Client entity) {
        entity.setClientId(dto.getClientId())
                .setClientSecret(dto.getClientSecret())
                .setRedirectUri(dto.getRedirectUri())
                .setGrantTypes(grantTypeTransformer.fromStringList(dto.getGrantTypes()))
                .setScopes(scopeTransformer.fromStringList(dto.getScopes()))
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
