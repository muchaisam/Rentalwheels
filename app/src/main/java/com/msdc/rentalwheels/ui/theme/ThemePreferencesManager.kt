package com.msdc.rentalwheels.ui.theme

import android.content.Context
import androidx.compose.runtime.compositionLocalOf

enum class ThemePreference {
    SYSTEM_DEFAULT,
    LIGHT,
    DARK
}

val LocalThemePreference = compositionLocalOf { ThemePreference.SYSTEM_DEFAULT }

class ThemePreferencesManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val THEME_KEY = "theme_preference"
    }

    fun getThemePreference(): ThemePreference {
        val preference = prefs.getString(THEME_KEY, ThemePreference.SYSTEM_DEFAULT.name)
        return try {
            ThemePreference.valueOf(preference ?: ThemePreference.SYSTEM_DEFAULT.name)
        } catch (e: IllegalArgumentException) {
            ThemePreference.SYSTEM_DEFAULT
        }
    }

    fun setThemePreference(preference: ThemePreference) {
        prefs.edit().putString(THEME_KEY, preference.name).apply()
    }
}
