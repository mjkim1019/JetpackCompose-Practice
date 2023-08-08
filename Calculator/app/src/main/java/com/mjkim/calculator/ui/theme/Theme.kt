package com.mjkim.calculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColorScheme(
    primary = dark_primary,
    onPrimary = dark_onPrimary,
    primaryContainer = dark_primaryContainer,
)

private val LightColorPalette = lightColorScheme(
    primary = light_primary,
    onPrimary = light_onPrimary,
    primaryContainer = light_primaryContainer
)

@Composable
fun CalculatorTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val calculatorColorScheme = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = calculatorColorScheme,
        typography = calculatorTypography,
        shapes = calculatorShapes,
        content = content
    )
}