package com.pokeadmin.adminteampokemon.pokemon.dto;

import com.pokeadmin.adminteampokemon.integration.pokemon.model.PokemonInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PokedexSearchInfoResponse {
    

    private Integer numPokedex;
    private String nombre;
    private String imageUrl;

    public PokedexSearchInfoResponse(PokemonInfo pokemon){
        this.numPokedex = pokemon.getNumPokedex();
        this.nombre =pokemon.getNombre();
        this.imageUrl= pokemon.getImageUrl();
    }
}
