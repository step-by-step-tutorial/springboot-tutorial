package com.tutorial.springboot.securityoauth2server.api.impl;

import com.tutorial.springboot.securityoauth2server.api.AllApi;
import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.service.impl.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutorial.springboot.securityoauth2server.util.SecurityUtils.getCurrentUsername;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientApi extends AllApi<Long, Client, ClientDto> {

    protected ClientApi(ClientService service) {
        super(service);
    }

    @Override
    protected ClientService getService() {
        return (ClientService) service;
    }

    @GetMapping("/findByClientId/{clientId}")
    public ResponseEntity<ClientDto> getClientByClientId(@PathVariable String clientId) {
        logger.info("Received an inbound request to get client:{} of username:{}", getCurrentUsername(), clientId);
        return getService().getByClientId(clientId)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
