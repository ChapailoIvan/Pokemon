package by.chapailo.pokemons.common

import androidx.compose.runtime.MutableState

fun <T> MutableState<T>.update(
    newValue: T? = null,
    transform: (T.() -> T)? = null
) {
    if (newValue != null)
        this.value = newValue
    else if (transform != null) {
        this.value = this.value.transform()
    }
}