package by.chapailo.pokemons.presentation.details_screen

import androidx.annotation.DimenRes
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import by.chapailo.pokemons.R
import by.chapailo.pokemons.common.Resource
import by.chapailo.pokemons.presentation.PokemonUiEntity
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun PokemonDetailsScreen(
    state: State<Resource<PokemonUiEntity?>>,
    navigateBack: () -> Unit,
    onEventAction: (PokemonDetailsScreenEvent) -> Unit
) {
    rememberSystemUiController().also {
        it.setStatusBarColor(
            color = MaterialTheme.colors.background
        )
        it.setNavigationBarColor(
            color = MaterialTheme.colors.primary
        )
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
                    errorMessage = stringResource(id = R.string.retrieving_data_error_try_again),
                    navigateBack = navigateBack,
                    onRetryAction = {
                        onEventAction(PokemonDetailsScreenEvent.TryAgain)
                    }
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
                .weight(0.75f)
                .background(color = MaterialTheme.colors.background),
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
                    style = MaterialTheme.typography.h3,
                    color = MaterialTheme.colors.onBackground
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.primary)
                .padding(dimensionResource(id = R.dimen.default_padding))
                .weight(0.25f),
            verticalArrangement = Arrangement.Center
        ) {
            if (pokemon != null) {
                StyledText(text = stringResource(id = R.string.pokemon_height, pokemon.height))

                Spacer(Modifier.height(dimensionResource(id = R.dimen.default_spacer_height)))

                StyledText(text = stringResource(id = R.string.pokemon_weight, pokemon.weight))

                Spacer(Modifier.height(dimensionResource(id = R.dimen.default_spacer_height)))

                StyledText(text = stringResource(id = R.string.pokemon_type, pokemon.types))
            } else {
                StyledText(text = stringResource(id = R.string.pokemon_no_information_found))
            }
        }

    }

}

@Composable
private fun PokemonDetailsScreenLoading(
    navigateBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        PokemonDetailsScreenNavigateBack(
            modifier = Modifier
                .align(Alignment.TopStart),
            navigateBack = navigateBack
        )

        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center),
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
private fun PokemonDetailsScreenError(
    errorMessage: String,
    navigateBack: () -> Unit,
    onRetryAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        PokemonDetailsScreenNavigateBack(
            modifier = Modifier
                .align(Alignment.TopStart),
            navigateBack = navigateBack
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.half_default_padding)),
                text = errorMessage,
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onRetryAction
            ) {
                Text(text = stringResource(id = R.string.button_retry))
            }
        }
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
            .padding(dimensionResource(id = R.dimen.half_default_padding))
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = MaterialTheme.colors.onBackground
        )
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
        fontSize = fontDimensionResource(id = R.dimen.styled_text_font_size),
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    )
}

@Composable
@ReadOnlyComposable
fun fontDimensionResource(@DimenRes id: Int) = dimensionResource(id = id).value.sp