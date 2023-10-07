package io.tklabs.sl_tax_calc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ColorsExample() {
    val coloors = listOf<Pair<Color, String>>(
        MaterialTheme.colorScheme.primary to "primary",
        MaterialTheme.colorScheme.primaryContainer to "primaryContainer",
        MaterialTheme.colorScheme.onPrimary to  "onPrimary",
        MaterialTheme.colorScheme.inversePrimary to "inversePrimary",
        MaterialTheme.colorScheme.primaryContainer to   "primaryContainer",
        MaterialTheme.colorScheme.background to "background",
        MaterialTheme.colorScheme.onBackground to "onBackground",
        MaterialTheme.colorScheme.error to "error",
        MaterialTheme.colorScheme.errorContainer to "errorContainer",
        MaterialTheme.colorScheme.onError to "onError",
        MaterialTheme.colorScheme.errorContainer to "errorContainer",
        MaterialTheme.colorScheme.surface to "surface",
        MaterialTheme.colorScheme.surfaceTint to "surfaceTint",
        MaterialTheme.colorScheme.surfaceVariant to     "surfaceVariant",
        MaterialTheme.colorScheme.onSurfaceVariant to "onSurfaceVariant",
        MaterialTheme.colorScheme.inverseOnSurface to "inverseOnSurface",
        MaterialTheme.colorScheme.inverseSurface to "inverseSurface",
        MaterialTheme.colorScheme.onSecondary to "onSecondary",
        MaterialTheme.colorScheme.secondaryContainer to     "secondaryContainer",
        MaterialTheme.colorScheme.onSecondary to "onSecondary",
        MaterialTheme.colorScheme.onSecondaryContainer to "onSecondaryContainer",
        MaterialTheme.colorScheme.scrim to "scrim",
        MaterialTheme.colorScheme.tertiary to "tertiary",
        MaterialTheme.colorScheme.tertiaryContainer to "tertiaryContainer",
        MaterialTheme.colorScheme.onTertiary to "onTertiary",
        MaterialTheme.colorScheme.onTertiaryContainer to "onTertiaryContainer",
    )
    LazyColumn(
        reverseLayout = false,
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(coloors){
            ClickableElevatedCardSample(it.first, it.second)
        }
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ClickableElevatedCardSample(color: Color = MaterialTheme.colorScheme.primary, title : String = "") {
    ElevatedCard(
        onClick = { /* Do something */ },
        modifier = Modifier.size(width = 280.dp, height = 50.dp)
    ) {
        Box(
            Modifier
                .background(color)
                .fillMaxSize()) {
            Text("$title", Modifier.align(Alignment.Center))
        }
    }
}
