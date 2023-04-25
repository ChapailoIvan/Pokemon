package by.chapailo.pokemons.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import by.chapailo.pokemons.data.util.Converters

@Database(
    entities = [
        PokemonDbEntity::class,
        PokemonDetailsDbEntity::class
    ], version = 1
)
@TypeConverters(Converters::class)
abstract class PokemonDatabase : RoomDatabase() {

    abstract val pokemonDao: PokemonDao
    abstract val pokemonDetailsDao: PokemonDetailsDao

}