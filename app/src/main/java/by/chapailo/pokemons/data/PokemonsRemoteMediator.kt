package by.chapailo.pokemons.data

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import by.chapailo.pokemons.data.local.PokemonDao
import by.chapailo.pokemons.data.local.PokemonDbEntity
import by.chapailo.pokemons.data.remote.PokemonApi
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class PokemonsRemoteMediator @Inject constructor(
    private val pokemonApi: PokemonApi,
    private val pokemonDao: PokemonDao
): RemoteMediator<Int, PokemonDbEntity>() {

    private var pageIndex = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PokemonDbEntity>
    ): MediatorResult {

        pageIndex = getPageIndex(loadType) ?:
            return MediatorResult.Success(endOfPaginationReached = true)

        val limit = state.config.pageSize
        val offset = pageIndex * limit

        return try {
            val pokemon = fetchPokemon(offset = offset, limit = limit)

            if (loadType == LoadType.REFRESH)
                pokemonDao.refresh(pokemon)
            else
                pokemonDao.save(pokemon)

            MediatorResult.Success(endOfPaginationReached = pokemon.size < limit)
        } catch (e: Throwable) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun fetchPokemon(
        offset: Int,
        limit: Int
    ): List<PokemonDbEntity> {
        return pokemonApi.getPokemons(
            offset = offset,
            limit = limit
        ).results
            .map { PokemonDbEntity(it) }
    }

    private fun getPageIndex(loadType: LoadType): Int? {
        pageIndex = when(loadType) {
            LoadType.REFRESH -> 0
            LoadType.APPEND -> ++pageIndex
            LoadType.PREPEND -> return null
        }
        return pageIndex
    }
}
