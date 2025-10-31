package com.msdc.rentalwheels.appnavigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavDestination
import com.msdc.rentalwheels.ui.components.PremiumBottomNavigation
import com.msdc.rentalwheels.ui.theme.ThemeState

@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
    themeState: ThemeState
) {
    // Use our PremiumBottomNavigation component
    PremiumBottomNavigation(
        currentDestination = currentDestination,
        onNavigate = onNavigate,
        themeState = themeState
    )
}
