package by.chapailo.pokemons.data.repositories

import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.data.local.PokemonDetailsDbEntity
import kotlinx.coroutines.flow.Flow

interface PokemonDetailsRepository {

    fun fetchPokemonDetails(name: String, url: String):
            Flow<Resource<PokemonDetailsDbEntity?>>

}