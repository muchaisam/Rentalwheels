package com.msdc.rentalwheels.ui.theme

import android.content.SharedPreferences
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberThemeState(initialTheme: ThemeMode = ThemeMode.System): ThemeState {
    val context = LocalContext.current
    val themePreferencesManager = remember { ThemePreferencesManager(context) }

    var savedPreference by remember { mutableStateOf(themePreferencesManager.getThemePreference()) }

    val themeState = remember {
        val themeMode =
                when (savedPreference) {
                    ThemePreference.LIGHT -> ThemeMode.Light
                    ThemePreference.DARK -> ThemeMode.Dark
                    ThemePreference.SYSTEM_DEFAULT -> ThemeMode.System
                }
        ThemeState(themeMode)
    }

    // Listen for SharedPreferences changes
    DisposableEffect(Unit) {
        val prefs =
                context.getSharedPreferences("theme_prefs", android.content.Context.MODE_PRIVATE)
        val listener =
                SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
                    if (key == "theme_preference") {
                        val newPreference = themePreferencesManager.getThemePreference()
                        savedPreference = newPreference

                        val newThemeMode =
                                when (newPreference) {
                                    ThemePreference.LIGHT -> ThemeMode.Light
                                    ThemePreference.DARK -> ThemeMode.Dark
                                    ThemePreference.SYSTEM_DEFAULT -> ThemeMode.System
                                }
                        themeState.setTheme(newThemeMode)
                    }
                }

        prefs.registerOnSharedPreferenceChangeListener(listener)

        onDispose { prefs.unregisterOnSharedPreferenceChangeListener(listener) }
    }

    return themeState
}
