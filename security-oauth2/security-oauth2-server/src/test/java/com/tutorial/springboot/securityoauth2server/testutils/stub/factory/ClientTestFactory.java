package com.tutorial.springboot.securityoauth2server.testutils.stub.factory;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.enums.GrantType;
import com.tutorial.springboot.securityoauth2server.transformer.ClientTransformer;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ClientTestFactory extends AbstractTestFactory<Long, Client, ClientDto> {

    public ClientTestFactory(ClientTransformer transformer) {
        super(transformer);
    }

    @Override
    public ClientDto newOne() {
        var name = faker.app().name().replace(" ", "_");
        return new ClientDto()
                .setClientId(name)
                .setClientSecret("password")
                .setRedirectUri("http://localhost:8080/login/oauth2/code/" + name)
                .setGrantTypes(GrantType.allType())
                .setScopes(Arrays.asList("read", "write"))
                .setAccessTokenValiditySeconds(3600)
                .setRefreshTokenValiditySeconds(1209600);
    }

}