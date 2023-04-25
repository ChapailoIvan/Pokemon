package by.chapailo.pokemons.data.util

import androidx.room.TypeConverter
import by.chapailo.pokemons.data.common.Sprites
import by.chapailo.pokemons.data.common.Type
import by.chapailo.pokemons.data.common.TypeX

class Converters {

    @TypeConverter
    fun stringFromTypesList(types: List<Type>): String {
        return types.joinToString(";") {
            listOf(
                it.type.name,
                it.type.url,
                it.slot.toString()
            ).joinToString(",")
        }
    }

    @TypeConverter
    fun stringToTypesList(data: String): List<Type> {
        return data.split(";").map { typeString ->
            typeString.split(",").run {
                Type(
                    type = TypeX(this[0], this[1]),
                    slot = this[2].toInt()
                )
            }
        }
    }

    @TypeConverter
    fun stringFromSprites(sprites: Sprites): String = sprites.front_default

    @TypeConverter
    fun stringToSprites(data: String): Sprites = Sprites(data)

}