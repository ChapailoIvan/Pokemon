package by.chapailo.pokemons.di

import android.content.Context
import androidx.room.Room
import by.chapailo.pokemons.common.Constants
import by.chapailo.pokemons.data.local.PokemonDao
import by.chapailo.pokemons.data.local.PokemonDatabase
import by.chapailo.pokemons.data.local.PokemonDetailsDao
import by.chapailo.pokemons.data.remote.PokemonApi
import by.chapailo.pokemons.data.repositories.DefaultPokemonDetailsRepository
import by.chapailo.pokemons.data.repositories.DefaultPokemonListRepository
import by.chapailo.pokemons.data.repositories.PokemonDetailsRepository
import by.chapailo.pokemons.data.repositories.PokemonListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    @Singleton
    fun providePokemonApi(): PokemonApi {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @Provides
    @Singleton
    fun providePokemonDatabase(
        @ApplicationContext context: Context
    ): PokemonDatabase {
        return Room.databaseBuilder(
            context,
            PokemonDatabase::class.java,
            PokemonDatabase.NAME
        ).build()
    }

    @Provides
    @Singleton
    fun providePokemonDatabasePokemonDao(
        pokemonDatabase: PokemonDatabase
    ): PokemonDao = pokemonDatabase.pokemonDao

    @Provides
    @Singleton
    fun providePokemonDatabasePokemonDetailsDao(
        pokemonDatabase: PokemonDatabase
    ): PokemonDetailsDao = pokemonDatabase.pokemonDetailsDao

}

@Module
@InstallIn(SingletonComponent::class)
interface ApplicationBindings {

    @Binds
    fun bindDefaultPokemonDetailsRepositoryToPokemonDetailsRepository(
        defaultPokemonDetailsRepository: DefaultPokemonDetailsRepository
    ): PokemonDetailsRepository

    @Binds
    fun bindDefaultPokemonListRepositoryToPokemonListRepository(
        defaultPokemonListRepository: DefaultPokemonListRepository
    ): PokemonListRepository

}