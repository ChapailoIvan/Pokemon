package by.chapailo.pokemons.data.repositories

import androidx.paging.PagingData
import by.chapailo.pokemons.data.local.PokemonDbEntity
import kotlinx.coroutines.flow.Flow

interface PokemonListRepository {

    fun fetchPagingPokemonList(): Flow<PagingData<PokemonDbEntity>>

}