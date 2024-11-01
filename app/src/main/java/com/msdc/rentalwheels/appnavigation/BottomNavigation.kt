package com.msdc.rentalwheels.appnavigation

import androidx.compose.foundation.Image
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavDestination
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.appnavigation.Screen
import com.msdc.rentalwheels.ui.theme.ThemeMode
import com.msdc.rentalwheels.ui.theme.ThemeState

@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
    themeState: ThemeState
) {
    val themeMode by themeState.themeMode
    val iconColor = if (themeMode == ThemeMode.Light) Color.Gray else Color.White

    NavigationBar(
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        NavigationBarItem(
            icon = { Image(painterResource(id = R.drawable.car_home), contentDescription = "Home") },
            label = { Text("Home") },
            selected = currentDestination?.route == Screen.Home.route,
            onClick = { onNavigate(Screen.Home) }
        )
        NavigationBarItem(
            icon = { Image(painterResource(id = R.drawable.maps), contentDescription = "Browse") },
            label = { Text("Browse") },
            selected = currentDestination?.route == Screen.Browse.route,
            onClick = { onNavigate(Screen.Browse) }
        )
        NavigationBarItem(
            icon = { Image(painterResource(id = R.drawable.bookings), contentDescription = "Library") },
            label = { Text("Bookings") },
            selected = currentDestination?.route == Screen.Bookings.route,
            onClick = { onNavigate(Screen.Bookings) }
        )
        NavigationBarItem(
            icon = { Image(painterResource(id = R.drawable.settings), contentDescription = "Settings") },
            label = { Text("Settings") },
            selected = currentDestination?.route == Screen.Settings.route,
            onClick = { onNavigate(Screen.Settings) }
        )
    }
}