package com.msdc.rentalwheels.uistates

import com.msdc.rentalwheels.ui.theme.ThemePreference

data class SettingsState(
    val isDarkMode: Boolean = false,
    val themePreference: ThemePreference = ThemePreference.SYSTEM_DEFAULT,
    val notificationsEnabled: Boolean = true,
    val biometricsEnabled: Boolean = false,
    val selectedLanguage: String = "English",
    val emailNotificationsEnabled: Boolean = true,
    val promotionalNotificationsEnabled: Boolean = true
)
