package by.chapailo.pokemons.presentation.list_screen

import androidx.compose.runtime.Composable
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow

@Composable
fun PokemonListScreen(
    pokemonList: Flow<PagingData<PokemonUiEntity>>,
    state: PokemonListScreenState,
    navigateToDetailsScreen: (name: String, url: String) -> Unit
) {

}