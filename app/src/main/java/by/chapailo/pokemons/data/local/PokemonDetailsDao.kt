package by.chapailo.pokemons.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PokemonDetailsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(pokemonDetails: PokemonDetailsDbEntity)

    @Query("SELECT * FROM pokemon_details WHERE name= :name")
    fun getBy(name: String): Flow<PokemonDetailsDbEntity?>

    @Delete
    suspend fun delete(pokemonDetails: PokemonDetailsDbEntity)

}