package by.chapailo.pokemons.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import by.chapailo.pokemons.data.common.PokemonDetails
import by.chapailo.pokemons.data.common.Sprites
import by.chapailo.pokemons.data.common.Type
import by.chapailo.pokemons.data.remote.PokemonDetailsNetworkEntity

@Entity(tableName = "pokemon_details")
data class PokemonDetailsDbEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val height: Int,
    val name: String,
    val sprites: Sprites,
    val types: List<Type>,
    val weight: Int
) {

    constructor(pokemonDetailsNetworkEntity: PokemonDetailsNetworkEntity) : this(
        id = pokemonDetailsNetworkEntity.id,
        height = pokemonDetailsNetworkEntity.height,
        name = pokemonDetailsNetworkEntity.name,
        sprites = pokemonDetailsNetworkEntity.sprites,
        types = pokemonDetailsNetworkEntity.types,
        weight = pokemonDetailsNetworkEntity.weight
    )

    fun toPokemonDetails(): PokemonDetails {
        return PokemonDetails(
            id = id,
            height = height,
            name = name,
            sprites = sprites,
            types = types,
            weight = weight
        )
    }

}