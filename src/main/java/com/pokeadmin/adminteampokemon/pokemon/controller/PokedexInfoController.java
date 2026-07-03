package com.pokeadmin.adminteampokemon.pokemon.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pokeadmin.adminteampokemon.pokemon.dto.PokedexSearchInfoResponse;
import com.pokeadmin.adminteampokemon.pokemon.service.PokemonQueryService;

@RestController
@RequestMapping("/api/pokedex")
public class PokedexInfoController {
    private final PokemonQueryService pokemonQueryService;

    public PokedexInfoController(PokemonQueryService pokemonQueryService){
        this.pokemonQueryService = pokemonQueryService;
    }

    @GetMapping("/info/{numPokedex}")
    public ResponseEntity<PokedexSearchInfoResponse> getPokedexInfo(@PathVariable Integer numPokedex) {
        PokedexSearchInfoResponse pokedexSearchInfoResponse = pokemonQueryService.getInfoPokemon( numPokedex);
        return ResponseEntity.ok(pokedexSearchInfoResponse);
    }
}
