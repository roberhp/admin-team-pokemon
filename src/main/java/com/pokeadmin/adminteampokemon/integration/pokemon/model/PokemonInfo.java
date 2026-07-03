package com.pokeadmin.adminteampokemon.integration.pokemon.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PokemonInfo {

    private Integer numPokedex;
    private String nombre;
    private String imageUrl;

}
