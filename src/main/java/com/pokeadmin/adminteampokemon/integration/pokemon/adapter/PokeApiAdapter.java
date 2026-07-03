package com.pokeadmin.adminteampokemon.integration.pokemon.adapter;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.pokeadmin.adminteampokemon.common.exception.PokemonNotFoundInPokeApiException;
import com.pokeadmin.adminteampokemon.config.PokeApiProperties;
import com.pokeadmin.adminteampokemon.integration.pokemon.cache.PokemonCache;
import com.pokeadmin.adminteampokemon.integration.pokemon.client.PokemonClient;
import com.pokeadmin.adminteampokemon.integration.pokemon.dto.PokeApiPokemonResponse;
import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;

@Component  
public class PokeApiAdapter implements PokemonClient{
    
    private final RestClient restClient;
    private final PokeApiProperties properties;
    private final PokemonCache pokemonCache;

    public PokeApiAdapter(RestClient restClient,PokeApiProperties properties,PokemonCache pokemonCache){
        this.restClient = restClient;
        this.properties = properties;
        this.pokemonCache = pokemonCache;
    }

    @Override
    public PokemonInfo findPokemonByPokedexNumber(Integer numPokedex) {
        Optional<PokemonInfo> cachedPokemon = pokemonCache.find(numPokedex);
        if(cachedPokemon.isPresent())
            return cachedPokemon.get();

        try {
            PokeApiPokemonResponse response = restClient.get()
                                                        .uri(properties.getBaseUrl() + "/pokemon/" + numPokedex)
                                                        .retrieve()
                                                        .body(PokeApiPokemonResponse.class);
            if (response == null) {
                throw new PokemonNotFoundInPokeApiException();
            }
            PokemonInfo pokemon = new PokemonInfo(
                response.getId(),
                response.getName(),
                response.getSprites().getFrontDefault());
            pokemonCache.save(pokemon);
            return pokemon;
        } catch (RestClientResponseException ex) {
            if (ex.getStatusCode().value() == 404) {
                throw new PokemonNotFoundInPokeApiException();
            }
            throw ex;
        }
    }
    
}
