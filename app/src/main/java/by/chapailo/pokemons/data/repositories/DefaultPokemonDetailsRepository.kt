package by.chapailo.pokemons.data.repositories

import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.common.networkBoundResource
import by.chapailo.pokemons.data.local.PokemonDetailsDao
import by.chapailo.pokemons.data.local.PokemonDetailsDbEntity
import by.chapailo.pokemons.data.remote.PokemonApi
import by.chapailo.pokemons.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class DefaultPokemonDetailsRepository @Inject constructor(
    private val pokemonDetailsDao: PokemonDetailsDao,
    private val pokemonApi: PokemonApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : PokemonDetailsRepository {

    override fun fetchPokemonDetails(
        id: Int,
        name: String
    ): Flow<Resource<PokemonDetailsDbEntity?>> =
        networkBoundResource(
            query = {
                pokemonDetailsDao.getBy(name)
            },
            fetch = {
                pokemonApi.getPokemonDetails(id = id)
            },
            saveFetchedResult = {
                pokemonDetailsDao.save(PokemonDetailsDbEntity(it))
            },
            shouldFetch = { entity ->
                entity == null
            }
        ).flowOn(dispatcher)

}