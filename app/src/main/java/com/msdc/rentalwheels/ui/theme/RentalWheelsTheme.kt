package com.msdc.rentalwheels.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Professional Color Scheme using Design System
private val md_theme_light_primary = RentalWheelsDesignSystem.Colors.BrandPrimary
private val md_theme_light_onPrimary = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_light_primaryContainer =
    RentalWheelsDesignSystem.Colors.BrandPrimaryLight.copy(alpha = 0.3f)
private val md_theme_light_onPrimaryContainer = RentalWheelsDesignSystem.Colors.BrandPrimaryVariant
private val md_theme_light_secondary = RentalWheelsDesignSystem.Colors.BrandSecondary
private val md_theme_light_onSecondary = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_light_secondaryContainer =
    RentalWheelsDesignSystem.Colors.BrandSecondaryLight.copy(alpha = 0.3f)
private val md_theme_light_onSecondaryContainer =
    RentalWheelsDesignSystem.Colors.BrandSecondaryVariant
private val md_theme_light_tertiary = RentalWheelsDesignSystem.Colors.BrandWarning
private val md_theme_light_onTertiary = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_light_tertiaryContainer = RentalWheelsDesignSystem.Colors.WarningLight
private val md_theme_light_onTertiaryContainer = RentalWheelsDesignSystem.Colors.BrandWarning
private val md_theme_light_error = RentalWheelsDesignSystem.Colors.Error
private val md_theme_light_errorContainer = RentalWheelsDesignSystem.Colors.ErrorLight
private val md_theme_light_onError = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_light_onErrorContainer = RentalWheelsDesignSystem.Colors.Error
private val md_theme_light_background = RentalWheelsDesignSystem.Colors.Surface01
private val md_theme_light_onBackground = RentalWheelsDesignSystem.Colors.TextPrimary
private val md_theme_light_surface = RentalWheelsDesignSystem.Colors.Surface02
private val md_theme_light_onSurface = RentalWheelsDesignSystem.Colors.TextPrimary
private val md_theme_light_surfaceVariant = RentalWheelsDesignSystem.Colors.Surface03
private val md_theme_light_onSurfaceVariant = RentalWheelsDesignSystem.Colors.TextSecondary
private val md_theme_light_outline = RentalWheelsDesignSystem.Colors.Surface04
private val md_theme_light_inverseOnSurface = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_light_inverseSurface = RentalWheelsDesignSystem.Colors.TextPrimary
private val md_theme_light_inversePrimary = RentalWheelsDesignSystem.Colors.BrandPrimaryLight

private val md_theme_dark_primary = RentalWheelsDesignSystem.Colors.BrandPrimaryLight
private val md_theme_dark_onPrimary = RentalWheelsDesignSystem.Colors.BrandPrimaryVariant
private val md_theme_dark_primaryContainer = RentalWheelsDesignSystem.Colors.BrandPrimaryVariant
private val md_theme_dark_onPrimaryContainer = RentalWheelsDesignSystem.Colors.BrandPrimaryLight
private val md_theme_dark_secondary = RentalWheelsDesignSystem.Colors.BrandSecondaryLight
private val md_theme_dark_onSecondary = RentalWheelsDesignSystem.Colors.BrandSecondaryVariant
private val md_theme_dark_secondaryContainer = RentalWheelsDesignSystem.Colors.BrandSecondaryVariant
private val md_theme_dark_onSecondaryContainer = RentalWheelsDesignSystem.Colors.BrandSecondaryLight
private val md_theme_dark_tertiary = RentalWheelsDesignSystem.Colors.BrandWarning
private val md_theme_dark_onTertiary = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_dark_tertiaryContainer = RentalWheelsDesignSystem.Colors.BrandWarning
private val md_theme_dark_onTertiaryContainer = RentalWheelsDesignSystem.Colors.WarningLight
private val md_theme_dark_error = RentalWheelsDesignSystem.Colors.Error
private val md_theme_dark_errorContainer = RentalWheelsDesignSystem.Colors.Error
private val md_theme_dark_onError = RentalWheelsDesignSystem.Colors.TextOnBrand
private val md_theme_dark_onErrorContainer = RentalWheelsDesignSystem.Colors.ErrorLight
private val md_theme_dark_background = RentalWheelsDesignSystem.Colors.SurfaceDark01
private val md_theme_dark_onBackground = RentalWheelsDesignSystem.Colors.TextPrimaryDark
private val md_theme_dark_surface = RentalWheelsDesignSystem.Colors.SurfaceDark02
private val md_theme_dark_onSurface = RentalWheelsDesignSystem.Colors.TextPrimaryDark
private val md_theme_dark_surfaceVariant = RentalWheelsDesignSystem.Colors.SurfaceDark03
private val md_theme_dark_onSurfaceVariant = RentalWheelsDesignSystem.Colors.TextSecondaryDark
private val md_theme_dark_outline = RentalWheelsDesignSystem.Colors.SurfaceDark04
private val md_theme_dark_inverseOnSurface = Color(0xFF1A1C1E)
private val md_theme_dark_inverseSurface = Color(0xFFE3E2E6)
private val md_theme_dark_inversePrimary = Color(0xFF1E88E5)

private val LightColors =
    lightColorScheme(
        primary = md_theme_light_primary,
        onPrimary = md_theme_light_onPrimary,
        primaryContainer = md_theme_light_primaryContainer,
        onPrimaryContainer = md_theme_light_onPrimaryContainer,
        secondary = md_theme_light_secondary,
        onSecondary = md_theme_light_onSecondary,
        secondaryContainer = md_theme_light_secondaryContainer,
        onSecondaryContainer = md_theme_light_onSecondaryContainer,
        tertiary = md_theme_light_tertiary,
        onTertiary = md_theme_light_onTertiary,
        tertiaryContainer = md_theme_light_tertiaryContainer,
        onTertiaryContainer = md_theme_light_onTertiaryContainer,
        error = md_theme_light_error,
        errorContainer = md_theme_light_errorContainer,
        onError = md_theme_light_onError,
        onErrorContainer = md_theme_light_onErrorContainer,
        background = md_theme_light_background,
        onBackground = md_theme_light_onBackground,
        surface = md_theme_light_surface,
        onSurface = md_theme_light_onSurface,
        surfaceVariant = md_theme_light_surfaceVariant,
        onSurfaceVariant = md_theme_light_onSurfaceVariant,
        outline = md_theme_light_outline,
        inverseOnSurface = md_theme_light_inverseOnSurface,
        inverseSurface = md_theme_light_inverseSurface,
        inversePrimary = md_theme_light_inversePrimary,
    )

private val DarkColors =
    darkColorScheme(
        primary = md_theme_dark_primary,
        onPrimary = md_theme_dark_onPrimary,
        primaryContainer = md_theme_dark_primaryContainer,
        onPrimaryContainer = md_theme_dark_onPrimaryContainer,
        secondary = md_theme_dark_secondary,
        onSecondary = md_theme_dark_onSecondary,
        secondaryContainer = md_theme_dark_secondaryContainer,
        onSecondaryContainer = md_theme_dark_onSecondaryContainer,
        tertiary = md_theme_dark_tertiary,
        onTertiary = md_theme_dark_onTertiary,
        tertiaryContainer = md_theme_dark_tertiaryContainer,
        onTertiaryContainer = md_theme_dark_onTertiaryContainer,
        error = md_theme_dark_error,
        errorContainer = md_theme_dark_errorContainer,
        onError = md_theme_dark_onError,
        onErrorContainer = md_theme_dark_onErrorContainer,
        background = md_theme_dark_background,
        onBackground = md_theme_dark_onBackground,
        surface = md_theme_dark_surface,
        onSurface = md_theme_dark_onSurface,
        surfaceVariant = md_theme_dark_surfaceVariant,
        onSurfaceVariant = md_theme_dark_onSurfaceVariant,
        outline = md_theme_dark_outline,
        inverseOnSurface = md_theme_dark_inverseOnSurface,
        inverseSurface = md_theme_dark_inverseSurface,
        inversePrimary = md_theme_dark_inversePrimary,
    )

@Composable
fun RentalWheelsTheme(
    themeState: ThemeState? = null,
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val actualThemeState = themeState ?: rememberThemeState()
    val darkTheme = actualThemeState.isDarkTheme()

    val colorScheme =
        when {
            dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
                val context = LocalContext.current
                if (darkTheme) dynamicDarkColorScheme(context)
                else dynamicLightColorScheme(context)
            }

            darkTheme -> DarkColors
            else -> LightColors
        }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            // Make status bar transparent for edge-to-edge experience
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()

            val insetsController = WindowCompat.getInsetsController(window, view)
            // Adjust status bar content color based on theme
            insetsController.isAppearanceLightStatusBars = !darkTheme
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

// Overloaded version for simple usage
@Composable
fun RentalWheelsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val themeState = rememberThemeState(if (darkTheme) ThemeMode.Dark else ThemeMode.Light)

    RentalWheelsTheme(themeState = themeState, dynamicColor = dynamicColor, content = content)
}
