package com.lazygeniouz.air.ui.theme

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

private val DarkColorPalette = darkColors(
    primary = Purple200, primaryVariant = Purple700, secondary = Teal200
)

@Composable
fun AIRTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        shapes = Shapes,
        content = content,
        typography = Typography,
        colors = DarkColorPalette.copy(),
    )
}

@Composable
fun ThemedSurface(content: @Composable () -> Unit) {
    AIRTheme {
        Surface(
            content = content,
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background,
        )
    }
}