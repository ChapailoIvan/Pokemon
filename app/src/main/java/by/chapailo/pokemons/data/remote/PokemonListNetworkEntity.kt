package by.chapailo.pokemons.data.remote

data class PokemonListNetworkEntity(
    val count: Int,
    val results: List<PokemonNetworkEntity>
)