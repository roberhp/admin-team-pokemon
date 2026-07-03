package com.pokeadmin.adminteampokemon.integration.pokemon.cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;

class RedisPokemonCacheTest {

    private RedisTemplate<String, PokemonInfo> redisTemplate;
    private ValueOperations<String, PokemonInfo> valueOperations;
    private RedisPokemonCache redisPokemonCache;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(RedisTemplate.class);
        valueOperations = mock(ValueOperations.class);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        redisPokemonCache = new RedisPokemonCache(redisTemplate);
    }

    @Test
    void shouldReturnPokemonWhenExistsInCache() {
        PokemonInfo pokemon = new PokemonInfo(25, "Pikachu", "url");
        when(valueOperations.get("pokemon:25")).thenReturn(pokemon);

        Optional<PokemonInfo> result = redisPokemonCache.find(25);

        assertTrue(result.isPresent());
        assertEquals(25, result.get().getNumPokedex());
        verify(valueOperations).get("pokemon:25");
    }

    @Test
    void shouldReturnEmptyWhenPokemonDoesNotExist() {

        when(valueOperations.get("pokemon:25")).thenReturn(null);
        Optional<PokemonInfo> result = redisPokemonCache.find(25);

        assertTrue(result.isEmpty());
        verify(valueOperations).get("pokemon:25");
    }

    @Test
    void shouldSavePokemonInRedis() {

        PokemonInfo pokemon = new PokemonInfo(25, "Pikachu", "url");

        PokemonInfo result = redisPokemonCache.save(pokemon);

        assertEquals(pokemon, result);

        verify(valueOperations).set(
                                eq("pokemon:25"),
                                eq(pokemon),
                                any(Duration.class));
    }

}