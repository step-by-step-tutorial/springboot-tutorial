package com.tutorial.springboot.security_rbac_jwt.testutils.stub.factory;

import com.tutorial.springboot.security_rbac_jwt.dto.AbstractDto;
import com.tutorial.springboot.security_rbac_jwt.entity.AbstractEntity;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.ConversionHelper;
import com.tutorial.springboot.security_rbac_jwt.testutils.stub.VarcharHelper;
import com.tutorial.springboot.security_rbac_jwt.transformer.AbstractTransformer;
import net.datafaker.Faker;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.IntStream;

import static com.tutorial.springboot.security_rbac_jwt.util.CollectionUtils.removeDuplication;
import static com.tutorial.springboot.security_rbac_jwt.util.ReflectionUtils.identifyType;


public abstract class AbstractTestFactory<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    public static final int DEFAULT_MAX_RELATION = 3;

    protected final Faker faker = new Faker();

    private final Class<ENTITY> entityClass;

    private final Class<DTO> dtoClass;

    protected boolean uniqueRelations = false;

    protected boolean uniqueData = false;

    protected AbstractTransformer<ID, ENTITY, DTO> transformer;

    protected AbstractTestFactory(AbstractTransformer<ID, ENTITY, DTO> transformer) {
        this.transformer = transformer;
        this.transformer.allowToExposeSecureInformation();
        entityClass = identifyType(1, getClass());
        dtoClass = identifyType(2, getClass());
    }

    public ConversionHelper<ENTITY, DTO> newInstances(int number) {
        var dtos = IntStream.range(0, number).boxed()
                .map(index -> newOne())
                .toList();
        if (uniqueData) {
            dtos = removeDuplication(dtos, getComparator()).toList();
        }

        var entities = transformer.toEntityList(dtos);

        return new ConversionHelper<>(
                new VarcharHelper<>(entities.toArray(size -> (ENTITY[]) Array.newInstance(entityClass, size))),
                new VarcharHelper<>(dtos.toArray(size -> (DTO[]) Array.newInstance(dtoClass, size)))
        );
    }

    protected abstract Function<DTO, ?> getComparator();

    protected abstract DTO newOne();

    public int chooseRandom(int bound) {
        if (bound <= 0) {
            return 0;
        }

        var random = new Random();
        return random.nextInt(1, 1 + bound);
    }

    public AbstractTestFactory<ID, ENTITY, DTO> makeUniqueData() {
        uniqueData = true;
        return this;
    }

    public AbstractTestFactory<ID, ENTITY, DTO> resetUniqueData() {
        uniqueData = false;
        return this;
    }

    public AbstractTestFactory<ID, ENTITY, DTO> setUniqueData(boolean uniqueData) {
        this.uniqueData = uniqueData;
        return this;
    }


    public AbstractTestFactory<ID, ENTITY, DTO> makeUniqueRelations() {
        uniqueRelations = true;
        return this;
    }

    public AbstractTestFactory<ID, ENTITY, DTO> resetUniqueRelations() {
        uniqueRelations = false;
        return this;
    }

    public AbstractTestFactory<ID, ENTITY, DTO> setUniqueRelations(boolean uniqueRelations) {
        this.uniqueRelations = uniqueRelations;
        return this;
    }
}