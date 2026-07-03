package com.pokeadmin.adminteampokemon.integration.pokemon.cache;

import java.time.Duration;
import java.util.Optional;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;

@Component
public class RedisPokemonCache implements PokemonCache {
    private static final String KEY_PREFIX = "pokemon:";
    private static final Duration TTL = Duration.ofSeconds(20);
    private final RedisTemplate<String, PokemonInfo> redisTemplate;

    public RedisPokemonCache(RedisTemplate<String, PokemonInfo> redisTemplate) {
        this.redisTemplate = redisTemplate;

    }

    @Override
    public Optional<PokemonInfo> find(Integer pokedexNumber) {
        String key = KEY_PREFIX + pokedexNumber;
        PokemonInfo pokemon = redisTemplate.opsForValue().get(key);
        return Optional.ofNullable(pokemon);
    }

    @Override
    public PokemonInfo save(PokemonInfo pokemonInfo) {
        String key = KEY_PREFIX + pokemonInfo.getNumPokedex();
        redisTemplate.opsForValue().set(key, pokemonInfo, TTL);
        return pokemonInfo;

    }

}