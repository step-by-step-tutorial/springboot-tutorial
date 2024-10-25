package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.entity.Scope;
import org.springframework.stereotype.Component;

@Component
public class ClientTransformer extends AbstractTransformer<Long, Client, ClientDto> {

    @Override
    protected void copyEntityToDto(Client entity, ClientDto dto) {
        dto.setRegistrationId(entity.getRegistrationId())
                .setClientId(entity.getClientId())
                .setClientSecret(entity.getClientSecret())
                .setClientAuthenticationMethod(entity.getClientAuthenticationMethod())
                .setAuthorizationGrantType(entity.getAuthorizationGrantType())
                .setRedirectUri(entity.getRedirectUri())
                .setScopes(entity.getScopes().stream().map(Scope::getName).toList())
                .setClientName(entity.getClientName())
                .setIssuerUri(entity.getIssuerUri())
                .setAuthorizationUri(entity.getAuthorizationUri())
                .setTokenUri(entity.getTokenUri())
                .setJwkSetUri(entity.getJwkSetUri())
                .setConfigurationMetadata(entity.getConfigurationMetadata())
                .setUserInfoUri(entity.getUserInfoUri())
                .setUserNameAttributeName(entity.getUserNameAttributeName())
                .setAuthenticationMethod(entity.getAuthenticationMethod());
    }

    @Override
    protected void copyDtoToEntity(ClientDto dto, Client entity) {
        entity.setRegistrationId(dto.getRegistrationId())
                .setClientId(dto.getClientId())
                .setClientSecret(dto.getClientSecret())
                .setClientAuthenticationMethod(dto.getClientAuthenticationMethod())
                .setAuthorizationGrantType(dto.getAuthorizationGrantType())
                .setRedirectUri(dto.getRedirectUri())
                .setScopes(dto.getScopes().stream().map(s -> new Scope().setName(s)).toList())
                .setClientName(dto.getClientName())
                .setIssuerUri(dto.getIssuerUri())
                .setAuthorizationUri(dto.getAuthorizationUri())
                .setTokenUri(dto.getTokenUri())
                .setJwkSetUri(dto.getJwkSetUri())
                .setConfigurationMetadata(dto.getConfigurationMetadata())
                .setUserInfoUri(dto.getUserInfoUri())
                .setUserNameAttributeName(dto.getUserNameAttributeName())
                .setAuthenticationMethod(dto.getAuthenticationMethod());
    }


}
