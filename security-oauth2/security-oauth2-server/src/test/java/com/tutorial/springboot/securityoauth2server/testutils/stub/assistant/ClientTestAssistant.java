package com.tutorial.springboot.securityoauth2server.testutils.stub.assistant;

import com.tutorial.springboot.securityoauth2server.dto.ClientDto;
import com.tutorial.springboot.securityoauth2server.entity.Client;
import com.tutorial.springboot.securityoauth2server.service.impl.ClientService;
import com.tutorial.springboot.securityoauth2server.testutils.stub.ConversionHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.VarcharHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.factory.ClientTestFactory;
import com.tutorial.springboot.securityoauth2server.transformer.ClientTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static com.tutorial.springboot.securityoauth2server.testutils.SecurityTestUtils.loginToTestEnv;

@Component
@ActiveProfiles({"test", "h2"})
public class ClientTestAssistant {

    @Autowired
    private ClientService clientService;

    @Autowired
    private ClientTransformer transformer;

    @Autowired
    private ClientTestFactory factory;

    public ConversionHelper<Client, ClientDto> populate(int number) {
        loginToTestEnv();
        var newClients = factory.newInstances(number).dto().asList();
        var identifications = clientService.saveBatch(newClients);
        var dtoList = clientService.getBatch(identifications);
        var entityList = transformer.toEntityList(dtoList);
        return new ConversionHelper<>(
                new VarcharHelper<>(entityList.toArray(Client[]::new)),
                new VarcharHelper<>(dtoList.toArray(ClientDto[]::new))
        );
    }

    public ConversionHelper<Client, ClientDto> selectAllTest() {
        var dtoArray = clientService.getAll().toArray(ClientDto[]::new);
        var entityArray = transformer.toEntityArray(dtoArray);
        return new ConversionHelper<>(
                new VarcharHelper<>(entityArray),
                new VarcharHelper<>(dtoArray)
        );
    }


}
