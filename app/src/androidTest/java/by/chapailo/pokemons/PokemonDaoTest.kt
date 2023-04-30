package by.chapailo.pokemons

import androidx.paging.PagingSource
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import by.chapailo.pokemons.data.local.PokemonDao
import by.chapailo.pokemons.data.local.PokemonDatabase
import by.chapailo.pokemons.data.local.PokemonDbEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class PokemonDaoTest {

    private lateinit var database: PokemonDatabase
    private lateinit var dao: PokemonDao

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PokemonDatabase::class.java
        ).allowMainThreadQueries()
            .build()
        dao = database.pokemonDao
    }

    @After
    fun teardown() {
        database.close()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun savePokemon() = runTest {
        val pokemon = PokemonDbEntity(
            name = "pokemon",
            url = "http://pokemon/{id}/"
        )
        val expected = PagingSource.LoadResult.Page(
            data = listOf(pokemon),
            prevKey = null,
            nextKey = null
        )

        dao.save(listOf(pokemon))

        val actual = dao.fetch().load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        Assert.assertTrue(actual is PagingSource.LoadResult.Page)
        Assert.assertEquals(expected.data, (actual as PagingSource.LoadResult.Page).data)
    }

}