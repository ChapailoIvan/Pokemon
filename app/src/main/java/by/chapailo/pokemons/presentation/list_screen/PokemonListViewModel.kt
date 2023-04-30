package by.chapailo.pokemons.presentation.list_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import by.chapailo.pokemons.data.repositories.PokemonListRepository
import by.chapailo.pokemons.presentation.PokemonUiEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val pokemonListRepository: PokemonListRepository
) : ViewModel() {

    val pokemonPagingData: Flow<PagingData<PokemonUiEntity>> by lazy {
        pokemonListRepository.fetchPagingPokemonList()
            .map { pagingData ->
                run {
                    pagingData.map { dbEntity -> PokemonUiEntity(dbEntity.toPokemon()) }
                }
            }
            .cachedIn(viewModelScope)
    }

}