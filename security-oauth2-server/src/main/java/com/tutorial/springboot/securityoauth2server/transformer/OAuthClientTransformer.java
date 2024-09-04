package com.tutorial.springboot.securityoauth2server.transformer;

import com.tutorial.springboot.securityoauth2server.dto.OAuthClientDto;
import com.tutorial.springboot.securityoauth2server.entity.OAuthClient;
import com.tutorial.springboot.securityoauth2server.entity.Scope;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class OAuthClientTransformer extends AbstractTransformer<Long, OAuthClient, OAuthClientDto> {

    @Override
    protected void copyEntityToDto(OAuthClient entity, OAuthClientDto dto) {
        dto.setClientId(entity.getClientId());
        dto.setClientSecret(entity.getClientSecret());
        dto.setRedirectUri(entity.getRedirectUri());
        dto.setGrantTypes(entity.getGrantTypes().stream().toList());
        dto.setScopes(entity.getScopes().stream()
                .map(Scope::getName)
                .collect(Collectors.toList()));
        dto.setAccessTokenValiditySeconds(entity.getAccessTokenValiditySeconds());
        dto.setRefreshTokenValiditySeconds(entity.getRefreshTokenValiditySeconds());
    }

    @Override
    protected void copyDtoToEntity(OAuthClientDto dto, OAuthClient entity) {
        entity.setClientId(dto.getClientId());
        entity.setClientSecret(dto.getClientSecret());
        entity.setRedirectUri(dto.getRedirectUri());
        entity.setGrantTypes(dto.getGrantTypes().stream().toList());
        entity.setScopes(dto.getScopes().stream()
                .map(s -> {
                    Scope scope = new Scope();
                    scope.setName(s);
                    return scope;
                })
                .toList());
        entity.setAccessTokenValiditySeconds(dto.getAccessTokenValiditySeconds());
        entity.setRefreshTokenValiditySeconds(dto.getRefreshTokenValiditySeconds());
    }
}
