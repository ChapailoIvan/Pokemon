package by.chapailo.pokemons.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.chapailo.pokemons.data.common.Pokemon
import by.chapailo.pokemons.data.remote.PokemonNetworkEntity

@Entity(tableName = "pokemon")
data class PokemonDbEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val url: String
) {

    constructor(pokemonNetworkEntity: PokemonNetworkEntity) : this(
        name = pokemonNetworkEntity.name,
        url = pokemonNetworkEntity.url
    )

    fun toPokemon(): Pokemon {
        return Pokemon(
            name = name,
            url = url
        )
    }

}
