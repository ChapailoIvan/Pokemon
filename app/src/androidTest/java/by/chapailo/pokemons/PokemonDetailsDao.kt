package by.chapailo.pokemons

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import by.chapailo.pokemons.data.common.Sprites
import by.chapailo.pokemons.data.local.PokemonDatabase
import by.chapailo.pokemons.data.local.PokemonDetailsDao
import by.chapailo.pokemons.data.local.PokemonDetailsDbEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PokemonDetailsDaoTest {

    lateinit var database: PokemonDatabase
    lateinit var dao: PokemonDetailsDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.pokemonDetailsDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun savePokemonDetails() = runTest {
        val pokemonDetailsDbEntity = PokemonDetailsDbEntity(
            id = 1,
            height = 20,
            name = "pokemon",
            sprites = Sprites("some_url"),
            types = emptyList(),
            weight = 10
        )

        dao.save(pokemonDetailsDbEntity)

        val savedPokemon = dao.getBy(pokemonDetailsDbEntity.name).first()

        Assert.assertEquals(pokemonDetailsDbEntity, savedPokemon)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun deletePokemonDetails() = runTest {
        val pokemonDetailsDbEntity = PokemonDetailsDbEntity(
            id = 1,
            height = 20,
            name = "pokemon",
            sprites = Sprites("some_url"),
            types = emptyList(),
            weight = 10
        )

        dao.save(pokemonDetailsDbEntity)
        dao.delete(pokemonDetailsDbEntity)

        val pokemonFromDb = dao.getBy(pokemonDetailsDbEntity.name).first()

        Assert.assertEquals(null, pokemonFromDb)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun getByNameNoSuchPokemonNullPointer() = runTest {
        val expected: PokemonDetailsDbEntity? = null

        val actual = dao.getBy("poke-pokemon").first()

        Assert.assertEquals(expected, actual)
    }
}

