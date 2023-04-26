package by.chapailo.pokemons.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.Url

interface PokemonApi {

    @GET("api/v2/pokemon")
    suspend fun getPokemons(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int = 20
    ): PokemonListNetworkEntity

    @GET("api/v2/pokemon/{id}")
    suspend fun getPokemonDetails(
        @Path("id") id: Int
    ): PokemonDetailsNetworkEntity

    @GET
    suspend fun getPokemonDetails(
        @Url link: String
    ): PokemonDetailsNetworkEntity

}