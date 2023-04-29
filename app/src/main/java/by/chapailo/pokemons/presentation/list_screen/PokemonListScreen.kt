package by.chapailo.pokemons.presentation.list_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import by.chapailo.pokemons.R
import by.chapailo.pokemons.presentation.CollapsedTopBar
import by.chapailo.pokemons.presentation.ExpandedTopBar
import by.chapailo.pokemons.presentation.PokemonUiEntity
import by.chapailo.pokemons.presentation.ScreenEvent
import by.chapailo.pokemons.presentation.details_screen.StyledText
import kotlinx.coroutines.flow.Flow
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonListScreen(
    pokemonList: Flow<PagingData<PokemonUiEntity>>,
    navigateToDetailsScreen: (id: Int, name: String) -> Unit,
    onScreenEvent: (ScreenEvent) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val lazyPagingItems = pokemonList.collectAsLazyPagingItems()
    val lazyColumnState = rememberLazyListState()

    val isCollapsed: State<Boolean> = remember {
        derivedStateOf { lazyColumnState.firstVisibleItemIndex > 0 }
    }

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = { CollapsedTopBar(isCollapsed = isCollapsed.value) }
    ) { padding ->
        CompositionLocalProvider(LocalOverscrollConfiguration provides null) {
            LazyColumn(
                state = lazyColumnState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                if (isCollapsed.value.not())
                    item { ExpandedTopBar() }

                items(
                    items = lazyPagingItems,
                    key = { item -> item.id }
                ) { pokemon ->
                    if (pokemon != null)
                        PokemonListScreenItem(
                            pokemon = pokemon,
                            navigateToDetailsScreen = navigateToDetailsScreen
                        )
                }

                item {
                    when (val state = lazyPagingItems.loadState.append) {
                        is LoadState.Loading ->
                            PokemonListScreenLoading()
                        is LoadState.Error ->
                            PokemonListScreenError(
                                errorMessage = state.error.localizedMessage ?: "",
                                onScreenEvent = onScreenEvent
                            )
                        is LoadState.NotLoading ->
                            PokemonListScreenError(
                                onScreenEvent = onScreenEvent,
                                errorMessage = stringResource(
                                    if (state.endOfPaginationReached)
                                        R.string.end_of_pagination_reached
                                    else
                                        R.string.load_state_not_loading_message
                                )
                            )
                    }
                }
            }
        }
    }
}

@Composable
private fun PokemonListScreenItem(
    pokemon: PokemonUiEntity,
    navigateToDetailsScreen: (id: Int, name: String) -> Unit
) {
    Text(
        text = pokemon.name.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.ROOT
            ) else it.toString()
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
            .wrapContentHeight()
            .clickable {
                navigateToDetailsScreen(pokemon.id, pokemon.name)
            }
    )

    Divider(
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
private fun PokemonListScreenError(
    errorMessage: String,
    onScreenEvent: (ScreenEvent) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        StyledText(
            modifier = Modifier
                .padding(10.dp),
            text = errorMessage,
            color = Color.DarkGray
        )

        Button(onClick = { onScreenEvent(ScreenEvent.TryAgain) }) {
            Text(text = "Try again")
        }
    }
}

@Composable
private fun PokemonListScreenLoading() {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.Center)
        )
    }
}