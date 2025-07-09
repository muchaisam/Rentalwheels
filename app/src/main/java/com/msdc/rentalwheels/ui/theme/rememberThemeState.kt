package com.msdc.rentalwheels.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberThemeState(initialTheme: ThemeMode = ThemeMode.System): ThemeState {
    val context = LocalContext.current
    val themePreferencesManager = remember { ThemePreferencesManager(context) }
    val savedPreference = remember { themePreferencesManager.getThemePreference() }

    val themeMode =
        when (savedPreference) {
            ThemePreference.LIGHT -> ThemeMode.Light
            ThemePreference.DARK -> ThemeMode.Dark
            ThemePreference.SYSTEM_DEFAULT -> ThemeMode.System
        }

    return remember { ThemeState(themeMode) }
}
