package by.chapailo.pokemons.data.remote

import by.chapailo.pokemons.data.common.Sprites
import by.chapailo.pokemons.data.common.Type

data class PokemonDetailsNetworkEntity(
    val id: Int,
    val height: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<Type>,
    val weight: Int
)