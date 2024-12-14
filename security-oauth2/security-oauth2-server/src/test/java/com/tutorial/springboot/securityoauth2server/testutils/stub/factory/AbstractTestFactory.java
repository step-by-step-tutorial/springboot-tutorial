package com.tutorial.springboot.securityoauth2server.testutils.stub.factory;

import com.tutorial.springboot.securityoauth2server.dto.AbstractDto;
import com.tutorial.springboot.securityoauth2server.entity.AbstractEntity;
import com.tutorial.springboot.securityoauth2server.testutils.stub.ConversionHelper;
import com.tutorial.springboot.securityoauth2server.testutils.stub.VarcharHelper;
import com.tutorial.springboot.securityoauth2server.transformer.AbstractTransformer;
import net.datafaker.Faker;

import java.lang.reflect.Array;
import java.util.Random;
import java.util.stream.IntStream;

import static com.tutorial.springboot.securityoauth2server.util.ReflectionUtils.identifyType;

public abstract class AbstractTestFactory<ID, ENTITY extends AbstractEntity<ID, ENTITY>, DTO extends AbstractDto<ID, DTO>> {

    protected final Faker faker = new Faker();

    private final Class<ENTITY> entityClass;

    private final Class<DTO> dtoClass;

    protected AbstractTransformer<ID, ENTITY, DTO> transformer;

    protected AbstractTestFactory(AbstractTransformer<ID, ENTITY, DTO> transformer) {
        this.transformer = transformer;
        entityClass = identifyType(1, getClass());
        dtoClass = identifyType(2, getClass());
    }

    public ConversionHelper<ENTITY, DTO> newInstances(int number) {
        var dtoArray = IntStream.range(0, number).boxed()
                .map(index -> newOne())
                .toArray(size -> (DTO[]) Array.newInstance(dtoClass, size));

        var entityArray = transformer.toEntityArray(dtoArray);

        return new ConversionHelper<>(new VarcharHelper<>(entityArray), new VarcharHelper<>(dtoArray));
    }

    public abstract DTO newOne();

    public static int chooseRandom(int bound) {
        if (bound <= 0) {
            return 0;
        }

        var random = new Random();
        return random.nextInt(1, 1 + bound);
    }
}