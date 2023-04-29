package by.chapailo.pokemons.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val EXPANDED_TOP_BAR_HEIGHT = 200.dp
val COLLAPSED_TOP_BAR_HEIGHT = 56.dp

@Composable
fun ExpandedTopBar() {
    Box(
        modifier = Modifier
            .background(MaterialTheme.colors.primaryVariant)
            .fillMaxWidth()
            .height(EXPANDED_TOP_BAR_HEIGHT - COLLAPSED_TOP_BAR_HEIGHT),
        contentAlignment = Alignment.BottomStart
    ) {
        Text(
            modifier = Modifier.padding(16.dp),
            text = "Pokemon",
            color = MaterialTheme.colors.onPrimary,
            style = MaterialTheme.typography.h3
        )
    }
}

@Composable
fun CollapsedTopBar(
    modifier: Modifier = Modifier,
    isCollapsed: Boolean
) {
    val color: Color by animateColorAsState(
        if (isCollapsed) {
            MaterialTheme.colors.background
        } else {
            MaterialTheme.colors.primaryVariant
        }
    )

    Box(
        modifier = modifier
            .background(color)
            .fillMaxWidth()
            .height(COLLAPSED_TOP_BAR_HEIGHT)
            .padding(16.dp),
        contentAlignment = Alignment.BottomStart
    ) {
        AnimatedVisibility(visible = isCollapsed) {
            Text(text = "Pokemon", style = MaterialTheme.typography.h6)
        }
    }
}