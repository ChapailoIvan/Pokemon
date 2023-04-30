package by.chapailo.pokemons.presentation

import by.chapailo.pokemons.data.common.Pokemon
import by.chapailo.pokemons.data.common.PokemonDetails

data class PokemonUiEntity(
    val id: Int = -1,
    val name: String = "",
    val types: String = "",
    val weight: String = "",
    val height: String = "",
    val imageUrl: String = ""
) {

    constructor(pokemon: Pokemon) : this(
        id = retrieveIdFromUrl(pokemon.url),
        name = pokemon.name
    )

    constructor(pokemonDetails: PokemonDetails) : this(
        id = pokemonDetails.id,
        name = pokemonDetails.name,
        types = pokemonDetails.types.joinToString(", ") { type -> type.type.name },
        weight = pokemonDetails.weight.toString(),
        height = pokemonDetails.height.toString(),
        imageUrl = pokemonDetails.sprites.front_default
    )

    companion object {
        private fun retrieveIdFromUrl(url: String): Int {
            url.split("/").also {
                return it[it.lastIndex - 1].toInt()
            }
        }
    }

}