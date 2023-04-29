package by.chapailo.pokemons.presentation.list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import by.chapailo.pokemons.R
import by.chapailo.pokemons.common.ResourceProvider
import by.chapailo.pokemons.data.repositories.PokemonListRepository
import by.chapailo.pokemons.presentation.PokemonUiEntity
import by.chapailo.pokemons.presentation.ScreenEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonListRepository: PokemonListRepository,
    private val resourceProvider: ResourceProvider
) : ViewModel() {

    val pokemonPagingData: Flow<PagingData<PokemonUiEntity>> by lazy {
        pokemonListRepository.fetchPagingPokemonList()
            .cachedIn(viewModelScope)
            .map { pagingData ->
                run {
                    pagingData.map { dbEntity -> PokemonUiEntity(dbEntity) }
                }
            }
    }

    private val _events: Channel<String> = Channel()
    val events = _events.receiveAsFlow()

    private val onErrorAction: (String?) -> Unit = { errorMessage ->
        _events.trySend(
            element = errorMessage ?: resourceProvider.getString(R.string.unexpected_error)
        )
    }

    fun handle(event: ScreenEvent) {
        when(event) {
            is ScreenEvent.TryAgain -> {

            }
        }
    }
}