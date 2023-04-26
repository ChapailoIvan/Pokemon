package by.chapailo.pokemons

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import by.chapailo.pokemons.presentation.navigation.NavGraph
import by.chapailo.pokemons.ui.theme.PokemonsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val navController = rememberNavController()

            PokemonsTheme {
                NavGraph(navController = navController)
            }
        }
    }
}
