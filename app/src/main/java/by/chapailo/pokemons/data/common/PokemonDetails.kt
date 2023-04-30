package by.chapailo.pokemons.data.common

data class PokemonDetails(
    val id: Int,
    val height: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<Type>,
    val weight: Int
)