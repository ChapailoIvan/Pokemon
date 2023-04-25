package by.chapailo.pokemons

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import by.chapailo.pokemons.ui.theme.PokemonsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonsTheme {

            }
        }
    }
}
