package com.pokeadmin.adminteampokemon.pokemon.service;


import java.util.List;

import org.springframework.stereotype.Service;

import com.pokeadmin.adminteampokemon.capture.entity.CapturedPokemonEntity;
import com.pokeadmin.adminteampokemon.capture.entity.PokemonLocation;
import com.pokeadmin.adminteampokemon.capture.repository.CapturePokemonRepository;
import com.pokeadmin.adminteampokemon.integration.pokemon.client.PokemonClient;
import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;
import com.pokeadmin.adminteampokemon.pokemon.dto.PokedexSearchInfoResponse;
import com.pokeadmin.adminteampokemon.pokemon.dto.PokemonInfoResponse;
import com.pokeadmin.adminteampokemon.pokemon.dto.PokemonSummaryResponse;

@Service
public class PokemonQueryService {
    
    private final CapturePokemonRepository capturePokemonRepository;
    private final PokemonClient pokemonClient; 

    public PokemonQueryService(CapturePokemonRepository capturePokemonRepository,PokemonClient pokemonClient) {
        this.capturePokemonRepository = capturePokemonRepository;
        this.pokemonClient = pokemonClient;
    }

    public List<PokemonInfoResponse> findPokemonByLocation(Long trainerId, PokemonLocation location) {
        List<CapturedPokemonEntity> capturedPokemon = capturePokemonRepository.findByTrainerTrainerIdAndLocation(trainerId, location);
        
        return capturedPokemon.stream()
                .map(PokemonInfoResponse::new)
                .toList();
        
    }

    public PokemonSummaryResponse getTrainerPokemonSummary(Long trainerId) {
        long totalTeam = capturePokemonRepository.countByTrainerTrainerIdAndLocation(trainerId, PokemonLocation.TEAM);
        long totalStorage = capturePokemonRepository.countByTrainerTrainerIdAndLocation(trainerId, PokemonLocation.STORAGE);
        return new PokemonSummaryResponse(totalTeam, totalStorage);
    }

    public PokedexSearchInfoResponse getInfoPokemon(Integer numPokedex) {
        PokemonInfo pokemonInfo = pokemonClient.findPokemonByPokedexNumber(numPokedex);
        return  new PokedexSearchInfoResponse(pokemonInfo);
    }

}
