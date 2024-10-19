package com.tutorial.springboot.securityoauth2client.service.impl;

import com.tutorial.springboot.securityoauth2client.dto.ClientDto;
import com.tutorial.springboot.securityoauth2client.entity.Client;
import com.tutorial.springboot.securityoauth2client.repository.ClientRepository;
import com.tutorial.springboot.securityoauth2client.service.AbstractService;
import com.tutorial.springboot.securityoauth2client.transformer.ClientTransformer;
import org.springframework.stereotype.Service;

@Service
public class ClientService extends AbstractService<Long, Client, ClientDto> {

    public ClientService(ClientRepository repository, ClientTransformer transformer) {
        super(repository, transformer);
    }
}
