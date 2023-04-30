package by.chapailo.pokemons.presentation.list_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.LocalOverscrollConfiguration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.items
import by.chapailo.pokemons.R
import by.chapailo.pokemons.presentation.CollapsedTopBar
import by.chapailo.pokemons.presentation.ExpandedTopBar
import by.chapailo.pokemons.presentation.PokemonUiEntity
import by.chapailo.pokemons.presentation.details_screen.StyledText
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlinx.coroutines.flow.Flow
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PokemonListScreen(
    pokemonList: Flow<PagingData<PokemonUiEntity>>,
    navigateToDetailsScreen: (id: Int, name: String) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val lazyPagingItems = pokemonList.collectAsLazyPagingItems()
    val lazyColumnState = lazyPagingItems.rememberLazyListState()

    rememberSystemUiController().also {
        it.setStatusBarColor(
            color = MaterialTheme.colors.primary
        )
        it.setNavigationBarColor(
            color = MaterialTheme.colors.background
        )
    }

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
                                errorMessage = stringResource(id = R.string.retrieving_data_error_try_again),
                                onRetryAction = lazyPagingItems::retry
                            )
                        is LoadState.NotLoading ->
                            if (state.endOfPaginationReached) {
                                PokemonListScreenError(
                                    onRetryAction = lazyPagingItems::retry,
                                    errorMessage = stringResource(
                                        R.string.end_of_pagination_reached
                                    )
                                )
                            }
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
        text = pokemon.name,
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.default_padding))
            .wrapContentHeight()
            .clickable {
                navigateToDetailsScreen(pokemon.id, pokemon.name)
            }
    )

    Divider(
        color = Color.Gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = dimensionResource(id = R.dimen.divider_horizontal_padding))
    )
}

@Composable
private fun PokemonListScreenError(
    errorMessage: String,
    onRetryAction: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.half_default_padding))
            .background(color = MaterialTheme.colors.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        StyledText(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.half_default_padding)),
            text = errorMessage,
            color = MaterialTheme.colors.onBackground
        )

        Button(onClick = onRetryAction) {
            Text(text = stringResource(id = R.string.button_retry))
        }
    }
}

@Composable
private fun PokemonListScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background)
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .padding(dimensionResource(id = R.dimen.half_default_padding))
                .align(Alignment.Center)
        )
    }
}

@Composable
fun <T : Any> LazyPagingItems<T>.rememberLazyListState(): LazyListState {
    return when (itemCount) {
        0 -> remember(this) { LazyListState(0, 0) }
        else -> androidx.compose.foundation.lazy.rememberLazyListState()
    }
}