package by.chapailo.pokemons.presentation.details_screen

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.chapailo.pokemons.common.Constants.POKEMON_ID_KEY
import by.chapailo.pokemons.common.Constants.POKEMON_NAME_KEY
import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.common.Resource.Companion.map
import by.chapailo.pokemons.common.update
import by.chapailo.pokemons.data.repositories.PokemonDetailsRepository
import by.chapailo.pokemons.presentation.PokemonUiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class PokemonDetailsViewModel @Inject constructor(
    private val pokemonDetailsRepository: PokemonDetailsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val pokemonId: Int = checkNotNull(savedStateHandle[POKEMON_ID_KEY])
    private val pokemonName: String = checkNotNull(savedStateHandle[POKEMON_NAME_KEY])

    private val _state = mutableStateOf<Resource<PokemonUiEntity?>>(Resource.Loading())
    val state: State<Resource<PokemonUiEntity?>> = _state

    init {
        fetchPokemonDetails()
    }

    fun handle(event: PokemonDetailsScreenEvent) {
        when (event) {
            is PokemonDetailsScreenEvent.TryAgain -> {
                fetchPokemonDetails()
            }
        }
    }

    private fun fetchPokemonDetails() {
        pokemonDetailsRepository.fetchPokemonDetails(
            id = pokemonId,
            name = pokemonName
        ).onEach { resource ->
            _state.update(newValue = resource.map { it?.let { PokemonUiEntity(it.toPokemonDetails()) } })
        }.launchIn(viewModelScope)
    }
}