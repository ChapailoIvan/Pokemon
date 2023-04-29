package by.chapailo.pokemons.presentation.details_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.chapailo.pokemons.R
import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.common.Resource.Companion.map
import by.chapailo.pokemons.common.ResourceProvider
import by.chapailo.pokemons.common.update
import by.chapailo.pokemons.data.repositories.PokemonDetailsRepository
import by.chapailo.pokemons.presentation.PokemonUiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val pokemonDetailsRepository: PokemonDetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle["id"])
    private val pokemonName: String = checkNotNull(savedStateHandle["name"])

    private val _state = mutableStateOf<Resource<PokemonUiEntity?>>(Resource.Loading())
    val state: State<Resource<PokemonUiEntity?>> = _state

    private val _events: Channel<String> = Channel()
    val events = _events.receiveAsFlow()

    private val onErrorAction: (String?) -> Unit = { errorMessage ->
        _events.trySend(
            element = errorMessage ?: resourceProvider.getString(R.string.unexpected_error)
        )
    }

    init {
        fetchPokemonDetails()
    }

    private fun fetchPokemonDetails() {
        pokemonDetailsRepository.fetchPokemonDetails(
            id = pokemonId,
            name = pokemonName
        ).onEach { resource ->
            if (resource.isErrorOccurredWhileRefreshingData())
                onErrorAction(resourceProvider.getString(R.string.refreshing_data_error))

            _state.update(newValue = resource.map { it?.let { PokemonUiEntity(it) } })
        }.launchIn(viewModelScope)
    }

    private fun <T> Resource<T>.isErrorOccurredWhileRefreshingData(): Boolean
        = this is Resource.Error && this.data != null
}