package by.chapailo.pokemons.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import by.chapailo.pokemons.common.Constants.POKEMON_ID_KEY
import by.chapailo.pokemons.common.Constants.POKEMON_NAME_KEY
import by.chapailo.pokemons.presentation.details_screen.PokemonDetailsScreen
import by.chapailo.pokemons.presentation.details_screen.PokemonDetailsViewModel
import by.chapailo.pokemons.presentation.list_screen.PokemonListScreen
import by.chapailo.pokemons.presentation.list_screen.PokemonListViewModel

@Composable
fun NavGraph(
    navController: NavHostController
) {

    NavHost(
        navController = navController,
        route = NAV_GRAPH_ROUTE,
        startDestination = POKEMON_LIST_SCREEN_ROUTE
    ) {

        composable(
            route = POKEMON_LIST_SCREEN_ROUTE
        ) {

            val viewModel: PokemonListViewModel = hiltViewModel()

            PokemonListScreen(
                pokemonList = viewModel.pokemonPagingData,
                navigateToDetailsScreen = { id, name ->
                    navController.navigate(
                        route = "${POKEMON_DETAILS_SCREEN_ROUTE}/$name/$id"
                    )
                }
            )
        }

        composable(
            route = "${POKEMON_DETAILS_SCREEN_ROUTE}/{name}/{id}",
            arguments = listOf(
                navArgument(name = POKEMON_NAME_KEY) { type = NavType.StringType },
                navArgument(name = POKEMON_ID_KEY) { type = NavType.IntType }
            )
        ) {

            val viewModel: PokemonDetailsViewModel = hiltViewModel()

            PokemonDetailsScreen(
                state = viewModel.state,
                navigateBack = {
                    navController.popBackStack()
                },
                onEventAction = viewModel::handle
            )
        }

    }

}