package com.tutorial.springboot.securityoauth2client.api.impl;

import com.tutorial.springboot.securityoauth2client.api.AllApi;
import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.service.impl.ClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.tutorial.springboot.securityoauth2client.util.SecurityUtils.getCurrentUsername;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientApi extends AllApi<Long, Client, ClientDto> {

    protected ClientApi(ClientService service) {
        super(service);
    }

    @GetMapping("/findByClientId/{clientId}")
    public ResponseEntity<ClientDto> getClientByClientId(@PathVariable String clientId) {
        logger.info("Received an inbound request to get client:{} of username:{}", getCurrentUsername(), clientId);
        return ((ClientService) service).getByClientId(clientId)
                .map(ResponseEntity::ok)
                .orElseThrow();
    }
}
