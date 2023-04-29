package by.chapailo.pokemons.presentation.details_screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.presentation.PokemonUiEntity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@Composable
fun PokemonDetailsScreen(
    state: State<Resource<PokemonUiEntity?>>,
    events: Flow<String>,
    navigateBack: () -> Unit
) {

    val context = LocalContext.current

    CollectWithLifecycle {
        events.collectLatest { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    when (val resource = state.value) {
        is Resource.Success -> PokemonDetailsScreenContent(
            pokemon = resource.data,
            navigateBack = navigateBack
        )
        is Resource.Loading -> {
            if (resource.data != null) {
                PokemonDetailsScreenContent(
                    pokemon = resource.data,
                    navigateBack = navigateBack
                )
            } else {
                PokemonDetailsScreenLoading(
                    navigateBack = navigateBack
                )
            }
        }
        is Resource.Error -> {
            if (resource.data != null) {
                PokemonDetailsScreenContent(
                    pokemon = resource.data,
                    navigateBack = navigateBack
                )
            } else {
                PokemonDetailsScreenError(
                    errorMessage = resource.throwable.localizedMessage ?: "Unexpected Error",
                    navigateBack = navigateBack
                )
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun PokemonDetailsScreenContent(
    modifier: Modifier = Modifier,
    pokemon: PokemonUiEntity?,
    navigateBack: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.75f),
            contentAlignment = Alignment.BottomStart
        ) {

            PokemonDetailsScreenNavigateBack(
                modifier = Modifier
                    .align(Alignment.TopStart),
                navigateBack = navigateBack
            )

            if (pokemon != null) {
                GlideImage(
                    model = pokemon.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentScale = ContentScale.FillWidth
                )

                Text(
                    modifier = Modifier.padding(20.dp),
                    text = pokemon.name,
                    style = MaterialTheme.typography.h3
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .padding(20.dp)
                .weight(0.25f),
            verticalArrangement = Arrangement.Center
        ) {
            if (pokemon != null) {
                StyledText(text = "Height: ${pokemon.height} m")

                Spacer(modifier = Modifier.height(10.dp))

                StyledText(text = "Weight: ${pokemon.weight} kg")

                Spacer(modifier = Modifier.height(10.dp))

                StyledText(text = "Type: ${pokemon.types}")
            } else {
                StyledText(text = "No information found")
            }
        }

    }

}

@Composable
private fun PokemonDetailsScreenLoading(
    navigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PokemonDetailsScreenNavigateBack(
            modifier = Modifier
                .align(Alignment.TopStart),
            navigateBack = navigateBack
        )

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun PokemonDetailsScreenError(
    errorMessage: String,
    navigateBack: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        PokemonDetailsScreenNavigateBack(
            modifier = Modifier
                .align(Alignment.TopStart),
            navigateBack = navigateBack
        )

        Text(
            text = errorMessage,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
private fun PokemonDetailsScreenNavigateBack(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit
) {
    IconButton(
        onClick = { navigateBack() },
        modifier = modifier
            .padding(10.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null
        )
    }
}

@Composable
fun CollectWithLifecycle(
    collectBlock: suspend () -> Unit
) {
    val lifecycle = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            launch {
                collectBlock()
            }
        }
    }
}

@Composable
fun StyledText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.onPrimary
) {
    Text(
        modifier = modifier,
        text = text,
        color = color,
        fontSize = 18.sp,
        fontWeight = FontWeight.Normal
    )
}