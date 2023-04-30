package by.chapailo.pokemons

import java.io.IOException
import java.io.InputStreamReader

object FileReader {
    fun readStringFromFile(fileName: String): String {
        try {
            val inputStream = this.javaClass.getResourceAsStream(fileName)
            val reader = InputStreamReader(inputStream, "UTF-8")
            return buildString {
                reader.readLines().forEach {
                    append(it)
                }
            }
        } catch (e: IOException) {
            throw e
        }
    }
}