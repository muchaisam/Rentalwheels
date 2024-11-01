package com.msdc.rentalwheels.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberThemeState(
    initialThemeMode: ThemeMode = ThemeMode.Light
): ThemeState {
    return remember {
        ThemeState(initialThemeMode)
    }
}