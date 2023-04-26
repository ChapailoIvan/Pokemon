package by.chapailo.pokemons.data.repositories

import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.common.networkBoundResource
import by.chapailo.pokemons.data.local.PokemonDetailsDao
import by.chapailo.pokemons.data.local.PokemonDetailsDbEntity
import by.chapailo.pokemons.data.remote.PokemonApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultPokemonDetailsRepository @Inject constructor(
    private val pokemonDetailsDao: PokemonDetailsDao,
    private val pokemonApi: PokemonApi,
) : PokemonDetailsRepository {

    override fun fetchPokemonDetails(
        name: String,
        url: String
    ): Flow<Resource<PokemonDetailsDbEntity?>> =
        networkBoundResource(
            query = {
                pokemonDetailsDao.getBy(name)
            },
            fetch = {
                pokemonApi.getPokemonDetails(link = url)
            },
            saveFetchedResult = {
                pokemonDetailsDao.save(PokemonDetailsDbEntity(it))
            },
            shouldFetch = { entity ->
                entity == null
            }
        )


}