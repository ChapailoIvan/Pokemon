package by.chapailo.pokemons

import by.chapailo.pokemons.data.common.Type
import by.chapailo.pokemons.data.util.Converters
import org.junit.Assert
import org.junit.Test

class ConvertersTest {

    @Test
    fun emptyTypeListConvertToStringEmptyString() {
        val types: List<Type> = emptyList()
        val expected = ""

        val actual = Converters().stringFromTypesList(types)

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun emptyStringConvertToTypeListEmptyList() {
        val emptyString = ""
        val expected: List<Type> = emptyList()

        val actual = Converters().stringToTypesList(emptyString)

        Assert.assertEquals(expected, actual)
    }
}