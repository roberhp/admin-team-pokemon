package com.pokeadmin.adminteampokemon.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, PokemonInfo> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, PokemonInfo> redisTemplate = new RedisTemplate<>();
        
        // ¿A qué Redis me voy a conectar?
        redisTemplate.setConnectionFactory(connectionFactory);

        // ¿Cómo serializo las llaves?
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // ¿Cómo serializo los valores?
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Opcional pero recomendable
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

        // Terminé de configurar el objeto
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

}