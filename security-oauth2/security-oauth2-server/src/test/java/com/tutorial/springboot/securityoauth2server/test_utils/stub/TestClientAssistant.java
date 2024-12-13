package com.tutorial.springboot.securityoauth2server.test_utils.stub;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.service.impl.ClientService;
import com.tutorial.springboot.securityoauth2server.transformer.ClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@ActiveProfiles({"test", "h2"})
public class TestClientAssistant {

    @Autowired
    ClientService clientService;

    @Autowired
    ClientTransformer clientTransformer;

    public ResultHelper<Client, ClientDto> insertTestClient(int number) {
        var dto = DtoStubFactory.createClient(number).asOne();
        clientService.save(dto);
        var entity = clientTransformer.toEntity(dto);
        return new ResultHelper<>(new StubHelper<>(entity), new StubHelper<>(dto));
    }

    public ResultHelper<Client, ClientDto> selectTestClient() {
        var dtoList = clientService.getAll();
        var entities = dtoList.stream().map(clientTransformer::toEntity).toList();
        return new ResultHelper<>(new StubHelper<>(entities.toArray(Client[]::new)), new StubHelper<>(dtoList.toArray(ClientDto[]::new)));
    }
}
