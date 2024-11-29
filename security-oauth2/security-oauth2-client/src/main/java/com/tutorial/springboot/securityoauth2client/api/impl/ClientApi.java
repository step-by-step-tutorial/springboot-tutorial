package com.tutorial.springboot.securityoauth2client.api.impl;

import com.tutorial.springboot.securityoauth2client.api.AllApi;
import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.service.impl.ClientService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientApi extends AllApi<Long, Client, ClientDto> {

    protected ClientApi(ClientService service) {
        super(service);
    }

}
