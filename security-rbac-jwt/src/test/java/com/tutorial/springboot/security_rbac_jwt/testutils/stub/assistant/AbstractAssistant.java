package com.tutorial.springboot.security_rbac_jwt.testutils.stub.assistant;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;
import com.tutorial.springboot.security_rbac_jwt.service.AbstractService;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.ConversionHelper;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.VarcharHelper;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory.AbstractTestFactory;
import com.tutorial.springboot.security_rbac_jwt.transformer.AbstractTransformer;

import java.lang.reflect.Array;
import java.util.function.Predicate;

import static com.tutorial.springboot.security_rbac_jwt.testutils.TestSecurityUtils.login;
import static com.tutorial.springboot.security_rbac_jwt.util.ReflectionUtils.identifyType;

public class AbstractAssistant<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    public static final int DEFAULT_MAX_RELATION = 3;

    protected final AbstractService<ID, ENTITY, DTO> service;

    protected final AbstractTransformer<ID, ENTITY, DTO> transformer;

    protected final AbstractTestFactory<ID, ENTITY, DTO> factory;

    private final Class<ENTITY> entityClass;

    private final Class<DTO> dtoClass;

    protected AbstractAssistant(AbstractService<ID, ENTITY, DTO> service, AbstractTransformer<ID, ENTITY, DTO> transformer, AbstractTestFactory<ID, ENTITY, DTO> factory) {
        this.service = service;
        this.transformer = transformer;
        this.transformer.allowToExposeSecureInformation();
        this.factory = factory;
        entityClass = identifyType(1, getClass());
        dtoClass = identifyType(2, getClass());
    }

    protected void beforePopulation() {
    }

    public ConversionHelper<ENTITY, DTO> populate(int number) {
        login();
        beforePopulation();
        var newDtos = factory.newInstances(number).dto().asList();
        var identities = service.saveBatch(newDtos);

        var dtos = service.getBatch(identities);
        var entities = transformer.toEntityList(dtos);

        return new ConversionHelper<>(
                new VarcharHelper<>(entities.toArray(size -> (ENTITY[])Array.newInstance(entityClass, size))),
                new VarcharHelper<>(dtos.toArray(size -> (DTO[]) Array.newInstance(dtoClass, size)))
        );
    }

    public ConversionHelper<ENTITY, DTO> select() {
        var dtos = service.getAll().stream()
                .filter(selectionFilter())
                .toList();

        var entities = transformer.toEntityList(dtos);

        return new ConversionHelper<>(
                new VarcharHelper<>(entities.toArray(size -> (ENTITY[])Array.newInstance(entityClass, size))),
                new VarcharHelper<>(dtos.toArray(size -> (DTO[]) Array.newInstance(dtoClass, size)))
        );
    }

    protected Predicate<DTO> selectionFilter() {
        return dto -> true;
    }

    public AbstractAssistant<ID, ENTITY, DTO> makeUniqueRelations() {
        this.factory.makeUniqueRelations();
        return this;
    }

    public AbstractAssistant<ID, ENTITY, DTO> resetUniqueRelations() {
        this.factory.resetUniqueRelations();
        return this;
    }

}
