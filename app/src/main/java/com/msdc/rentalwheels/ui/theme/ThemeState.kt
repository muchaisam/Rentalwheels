package com.msdc.rentalwheels.ui.theme

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class ThemeState(initialTheme: ThemeMode = ThemeMode.Light) {
    var themeMode by mutableStateOf(initialTheme)
        private set

    fun toggleTheme() {
        themeMode =
            when (themeMode) {
                ThemeMode.Light -> ThemeMode.Dark
                ThemeMode.Dark -> ThemeMode.Light
            }
    }

    fun setTheme(mode: ThemeMode) {
        themeMode = mode
    }
}