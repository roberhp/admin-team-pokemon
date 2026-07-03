package com.pokeadmin.adminteampokemon.pokemon.dto;

import com.pokeadmin.adminteampokemon.capture.entity.CapturedPokemonEntity;

import lombok.Data;

@Data
public class PokemonInfoResponse {
    
    private Long capturedPokemonId;
    private Integer pokedexNumber;
    private String nickname;

    public PokemonInfoResponse(CapturedPokemonEntity pokemon) {
        this.capturedPokemonId = pokemon.getCapturedPokemonId();
        this.pokedexNumber = pokemon.getPokedexNumber();
        this.nickname = pokemon.getNickname();
    }

}
