package com.msdc.rentalwheels.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ThemeState(initialTheme: ThemeMode = ThemeMode.System) {
    private val _themeMode = MutableStateFlow(initialTheme)
    val themeMode = _themeMode.asStateFlow()

    var currentThemeMode by mutableStateOf(initialTheme)
        private set

    fun toggleTheme() {
        val newMode =
            when (currentThemeMode) {
                ThemeMode.Light -> ThemeMode.Dark
                ThemeMode.Dark -> ThemeMode.System
                ThemeMode.System -> ThemeMode.Light
            }
        setTheme(newMode)
    }

    fun setTheme(mode: ThemeMode) {
        currentThemeMode = mode
        _themeMode.value = mode
    }

    @Composable
    fun isDarkTheme(): Boolean {
        return when (currentThemeMode) {
            ThemeMode.Light -> false
            ThemeMode.Dark -> true
            ThemeMode.System -> isSystemInDarkTheme()
        }
    }
}
