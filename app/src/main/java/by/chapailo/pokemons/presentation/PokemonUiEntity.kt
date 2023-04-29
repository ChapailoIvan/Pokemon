package by.chapailo.pokemons.presentation

import by.chapailo.pokemons.data.local.PokemonDbEntity
import by.chapailo.pokemons.data.local.PokemonDetailsDbEntity

data class PokemonUiEntity(
    val id: Int = -1,
    val name: String = "",
    val types: String = "",
    val weight: String = "",
    val height: String = "",
    val imageUrl: String = ""
) {

    constructor(pokemonDbEntity: PokemonDbEntity) : this(
        id = retrieveIdFromUrl(pokemonDbEntity.url),
        name = pokemonDbEntity.name
    )

    constructor(pokemonDetailsDbEntity: PokemonDetailsDbEntity) : this(
        id = pokemonDetailsDbEntity.id,
        name = pokemonDetailsDbEntity.name,
        types = pokemonDetailsDbEntity.types.joinToString(", ") { type -> type.type.name },
        weight = pokemonDetailsDbEntity.weight.toString(),
        height = pokemonDetailsDbEntity.height.toString(),
        imageUrl = pokemonDetailsDbEntity.sprites.front_default
    )

    companion object {
        private fun retrieveIdFromUrl(url: String): Int {
            url.split("/").also {
                return it[it.lastIndex - 1].toInt()
            }
        }
    }

}