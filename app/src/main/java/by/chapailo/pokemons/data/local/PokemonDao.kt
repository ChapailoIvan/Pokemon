package by.chapailo.pokemons.data.local

import androidx.paging.PagingSource
import androidx.room.*

@Dao
interface PokemonDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(pokemonDbEntities: List<PokemonDbEntity>)

    @Query("SELECT * FROM pokemon")
    fun fetch(): PagingSource<Int, PokemonDbEntity>

    @Query("DELETE FROM pokemon")
    suspend fun clear()

    @Transaction
    suspend fun refresh(pokemonDbEntities: List<PokemonDbEntity>) {
        clear()
        save(pokemonDbEntities)
    }

}