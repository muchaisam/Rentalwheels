package com.msdc.rentalwheels.ui.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf

class ThemeState(
    initialThemeMode: ThemeMode = ThemeMode.Light
) {
    private val _themeMode = mutableStateOf(initialThemeMode)
    val themeMode: State<ThemeMode> = _themeMode

    fun toggleTheme() {
        _themeMode.value = when (themeMode.value) {
            ThemeMode.Light -> ThemeMode.Dark
            ThemeMode.Dark -> ThemeMode.Light
        }
    }
}