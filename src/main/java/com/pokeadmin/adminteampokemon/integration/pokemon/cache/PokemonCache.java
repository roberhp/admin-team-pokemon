package com.pokeadmin.adminteampokemon.integration.pokemon.cache;

import java.util.Optional;

import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;

public interface PokemonCache {
    
    Optional<PokemonInfo> find (Integer PokedexNumber);

    PokemonInfo save(PokemonInfo pokemon);
}
