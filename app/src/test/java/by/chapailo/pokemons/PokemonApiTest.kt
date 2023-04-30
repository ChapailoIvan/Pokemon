package by.chapailo.pokemons

import by.chapailo.pokemons.common.Constants
import by.chapailo.pokemons.data.common.Sprites
import by.chapailo.pokemons.data.common.Type
import by.chapailo.pokemons.data.common.TypeX
import by.chapailo.pokemons.data.remote.PokemonApi
import by.chapailo.pokemons.data.remote.PokemonDetailsNetworkEntity
import by.chapailo.pokemons.data.remote.PokemonListNetworkEntity
import by.chapailo.pokemons.data.remote.PokemonNetworkEntity
import io.mockk.InternalPlatformDsl.toStr
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class PokemonApiTest {

    lateinit var webServer: MockWebServer
    lateinit var api: PokemonApi

    @Before
    fun setup() {
        webServer = MockWebServer()

        webServer.start()

        api = Retrofit.Builder()
            .baseUrl(webServer.url("/"))
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(PokemonApi::class.java)
    }

    @After
    fun tearDown() {
        webServer.shutdown()
    }

    @Test
    fun testRetrofitInstance() {
        val instance = Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
        Assert.assertEquals(instance.baseUrl().toStr(), Constants.BASE_URL)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPokemons() {
        webServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(GET_POKEMON_LIST_RESPONSE_BODY)
        )

        runTest {
            val actual = api.getPokemons(offset = 0, limit = 1)

            val expected = PokemonListNetworkEntity(
                count = 1281,
                results = listOf(
                    PokemonNetworkEntity(
                        name = "bulbasaur",
                        url = "https://pokeapi.co/api/v2/pokemon/1/"
                    )
                )
            )

            Assert.assertEquals(expected, actual)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testGetPokemonDetails() {
        webServer.enqueue(
            MockResponse()
                .setResponseCode(200)
                .setBody(FileReader.readStringFromFile("/test.json"))
        )

        runTest {
            val actual = api.getPokemonDetails(id = 1)

            val expected = PokemonDetailsNetworkEntity(
                id = 1,
                height = 7,
                weight = 69,
                sprites = Sprites(
                    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/1.png"
                ),
                types = listOf(
                    Type(
                        slot = 1,
                        type = TypeX("grass", "https://pokeapi.co/api/v2/type/12/")
                    ),
                    Type(
                        slot = 2,
                        type = TypeX("poison", "https://pokeapi.co/api/v2/type/4/")
                    )
                ),
                name = "bulbasaur"
            )

            Assert.assertEquals(expected, actual)
        }
    }


}