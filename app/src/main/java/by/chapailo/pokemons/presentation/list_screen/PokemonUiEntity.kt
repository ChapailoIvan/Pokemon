package by.chapailo.pokemons.presentation.list_screen

import by.chapailo.pokemons.data.local.PokemonDbEntity

data class PokemonUiEntity(
    val name: String
) {

    constructor(pokemonDbEntity: PokemonDbEntity) : this(name = pokemonDbEntity.name)

}