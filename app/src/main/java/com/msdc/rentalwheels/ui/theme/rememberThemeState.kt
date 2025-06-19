package com.msdc.rentalwheels.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberThemeState(
    initialTheme: ThemeMode = if (isSystemInDarkTheme()) ThemeMode.Dark else ThemeMode.Light
): ThemeState {
    return remember { ThemeState(initialTheme) }
}