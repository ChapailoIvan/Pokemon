package by.chapailo.pokemons.presentation.list_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import by.chapailo.pokemons.data.repositories.PokemonListRepository
import by.chapailo.pokemons.R
import by.chapailo.pokemons.common.ResourceProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
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

    private val _state = mutableStateOf(PokemonListScreenState())
    val state: State<PokemonListScreenState> = _state

    private val _events: Channel<String> = Channel()
    val events = _events.receiveAsFlow()

    private val onErrorAction: (String?) -> Unit = { errorMessage ->
        _events.trySend(
            element = errorMessage ?: resourceProvider.getString(R.string.unexpected_error)
        )
    }

    init {
        fetchPokemonList()
    }

    private fun fetchPokemonList() {
        /*pokemonListRepository.fetchPagingPokemonList()
            .cachedIn(viewModelScope)
            .catch { error ->
                onErrorAction(error.localizedMessage)
            }
            .onEach { pagingData -> run {
                _state.update {
                    copy(nameList = pagingData.map { dbEntity -> PokemonUiEntity(dbEntity) })
                }
            } }*/
    }

}