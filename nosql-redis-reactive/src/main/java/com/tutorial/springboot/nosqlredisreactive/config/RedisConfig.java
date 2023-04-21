package com.tutorial.springboot.nosqlredisreactive.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean("reactiveRedisConnectionFactory")
    public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean("redisTemplate")
    public ReactiveRedisTemplate<String, String> redisTemplate(@Qualifier("reactiveRedisConnectionFactory")
                                                                   ReactiveRedisConnectionFactory factory) {
            var keySerializer = new StringRedisSerializer();
            var valueSerializer = new Jackson2JsonRedisSerializer<>(String.class);
            var builder = RedisSerializationContext.<String, String>newSerializationContext(keySerializer);
            var context = builder
                    .value(valueSerializer)
                    .build();
            return new ReactiveRedisTemplate<>(factory, context);
    }
}
