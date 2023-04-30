package by.chapailo.pokemons.data.repositories

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import by.chapailo.pokemons.data.PokemonsRemoteMediator
import by.chapailo.pokemons.data.local.PokemonDao
import by.chapailo.pokemons.data.local.PokemonDbEntity
import by.chapailo.pokemons.data.remote.PokemonApi
import by.chapailo.pokemons.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultPokemonListRepository @Inject constructor(
    private val pokemonDao: PokemonDao,
    private val pokemonApi: PokemonApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PokemonListRepository {


    @OptIn(ExperimentalPagingApi::class)
    override fun fetchPagingPokemonList(): Flow<PagingData<PokemonDbEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE
            ),
            pagingSourceFactory = {
                pokemonDao.fetch()
            },
            remoteMediator = PokemonsRemoteMediator(
                pokemonApi, pokemonDao
            )
        ).flow.flowOn(dispatcher)
    }

    companion object {

        const val DEFAULT_PAGE_SIZE = 20

    }

}