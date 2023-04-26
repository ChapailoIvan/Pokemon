package by.chapailo.pokemons.presentation.details_screen

import by.chapailo.pokemons.data.local.PokemonDetailsDbEntity

data class PokemonDetailsScreenState(
    val name: String = "",
    val imageUrl: String = "",
    val types: List<String> = emptyList(),
    val weight: String = "",
    val height: String = "",
    val isLoading: Boolean = false,
    val isWithError: Boolean = false
) {

    constructor(
        isLoading: Boolean = false,
        isWithError: Boolean = false,
        pokemon: PokemonDetailsDbEntity
    ): this(
        name = pokemon.name,
        imageUrl = pokemon.sprites.front_default,
        types = pokemon.types.map { type -> type.type.name },
        weight = pokemon.weight.toString(),
        height = pokemon.height.toString(),
        isLoading = isLoading,
        isWithError = isWithError
    )

}