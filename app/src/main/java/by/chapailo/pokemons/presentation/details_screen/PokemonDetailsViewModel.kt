package by.chapailo.pokemons.presentation.details_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.chapailo.pokemons.R
import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.common.ResourceProvider
import by.chapailo.pokemons.common.update
import by.chapailo.pokemons.data.repositories.PokemonDetailsRepository
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

    private val pokemonUrl: String = checkNotNull(savedStateHandle["url"])
    private val pokemonName: String = checkNotNull(savedStateHandle["name"])

    private val _state = mutableStateOf(PokemonDetailsScreenState())
    val state: State<PokemonDetailsScreenState> = _state

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
            name = pokemonName,
            url = pokemonUrl
        ).onEach { resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let {
                        _state.update(newValue = PokemonDetailsScreenState(pokemon = it))
                    }
                }
                is Resource.Loading -> {
                    if (resource.data != null)
                        _state.update(newValue = PokemonDetailsScreenState(pokemon = resource.data))
                    else
                        _state.update { copy(isLoading = true) }
                }
                is Resource.Error -> {
                    if (resource.data != null)
                        _state.update(newValue = PokemonDetailsScreenState(pokemon = resource.data))
                    else
                        _state.update { copy(isWithError = true) }

                    onErrorAction(resource.error?.localizedMessage)
                }
            }
        }.launchIn(viewModelScope)
    }

}