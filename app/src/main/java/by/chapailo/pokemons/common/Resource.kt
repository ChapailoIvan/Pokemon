package by.chapailo.pokemons.common

sealed class Resource<T> {

    class Success<T>(val data: T) : Resource<T>()
    class Loading<T>(val data: T? = null) : Resource<T>()
    class Error<T>(val throwable: Throwable, val data: T? = null) : Resource<T>()

    companion object {
        fun <T, R> Resource<T>.map(block: (T) -> R): Resource<R> {
            return when (this) {
                is Success -> {
                    Success(data = block(this.data))
                }
                is Error -> {
                    Error(throwable = throwable, data = this.data?.let { block(it) })
                }
                is Loading -> {
                    Loading(data = this.data?.let { block(it) })
                }
            }
        }
    }
}