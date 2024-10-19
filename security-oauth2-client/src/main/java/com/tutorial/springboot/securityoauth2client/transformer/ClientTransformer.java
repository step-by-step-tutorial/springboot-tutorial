package com.tutorial.springboot.securityoauth2client.transformer;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.Client;
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
                .setScope(entity.getScope())
                .setClientName(entity.getClientName())
                .setIssuerUri(entity.getIssuerUri())
                .setAuthorizationUri(entity.getAuthorizationUri())
                .setTokenUri(entity.getTokenUri())
                .setJwkSetUri(entity.getJwkSetUri())
                .setConfigurationMetadata(entity.getConfigurationMetadata())
                .setUri(entity.getUri())
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
                .setScope(dto.getScope())
                .setClientName(dto.getClientName())
                .setIssuerUri(dto.getIssuerUri())
                .setAuthorizationUri(dto.getAuthorizationUri())
                .setTokenUri(dto.getTokenUri())
                .setJwkSetUri(dto.getJwkSetUri())
                .setConfigurationMetadata(dto.getConfigurationMetadata())
                .setUri(dto.getUri())
                .setUserNameAttributeName(dto.getUserNameAttributeName())
                .setAuthenticationMethod(dto.getAuthenticationMethod());
    }


}
