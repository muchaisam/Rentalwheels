package com.msdc.rentalwheels.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.msdc.rentalwheels.viewmodel.SettingsViewModel

@Composable
fun ThemeProvider(settingsViewModel: SettingsViewModel? = null, content: @Composable () -> Unit) {
    // Get theme from settingsViewModel or from settings state
    val settingsState by
    (settingsViewModel?.settingsState
        ?: run {
            val vm: SettingsViewModel = viewModel()
            vm.settingsState
        })
        .collectAsState()

    val useDarkTheme = settingsState.isDarkMode

    val context = LocalContext.current
    val colorScheme =
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && useDarkTheme -> {
                dynamicDarkColorScheme(context)
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !useDarkTheme -> {
                dynamicLightColorScheme(context)
            }

            useDarkTheme -> darkColorScheme()
            else -> lightColorScheme()
        }

    // Update status bar colors
    val view = LocalView.current
    if (!view.isInEditMode) {
        val window = (context as Activity).window
        DisposableEffect(useDarkTheme) {
            window.statusBarColor = colorScheme.surface.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars =
                !useDarkTheme
            onDispose {}
        }
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
