package com.msdc.rentalwheels.appnavigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.msdc.rentalwheels.ui.screens.BookingsScreen
import com.msdc.rentalwheels.ui.screens.BrowseScreen
import com.msdc.rentalwheels.ui.screens.DetailedCarScreen
import com.msdc.rentalwheels.ui.screens.HomeScreen
import com.msdc.rentalwheels.ui.screens.SettingsScreen
import com.msdc.rentalwheels.ui.theme.ThemeMode
import com.msdc.rentalwheels.ui.theme.ThemeState
import com.msdc.rentalwheels.ui.theme.rememberThemeState
import com.msdc.rentalwheels.viewmodel.CarViewModel


@Composable
fun RentalWheelsApp(
    viewModel: CarViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    themeState: ThemeState = rememberThemeState()
) {
    val themeMode by themeState.themeMode

    val colors = when (themeMode) {
        ThemeMode.Light -> lightColorScheme()
        ThemeMode.Dark -> darkColorScheme()
    }

    val uiState by viewModel.uiState.collectAsState()
    val carDetailState by viewModel.carDetailState.collectAsState()

    MaterialTheme(
        colorScheme = colors,
        typography = Typography()
    ) {
        Scaffold(
            bottomBar = {
                BottomNavigation(
                    currentDestination = navController.currentDestination,
                    onNavigate = { screen ->
                        navController.navigate(screen.route)
                    },
                    themeState = themeState
                )
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        uiState = uiState,
                        carDetailState = carDetailState,
                        onCarClick = { carId -> viewModel.loadCarDetails(carId) },
                        onLoadMore = { viewModel.loadMoreCars() },
                        onBackClick = { viewModel.clearCarDetails() },
                        onRefresh = { viewModel.refreshData() },
                        onSearch = {  }
                    )
                }
                composable(Screen.Browse.route) {
                    BrowseScreen()
                }
//                composable(
//                    route = "${Screen.DetailedCarScreen.route}/{placeId}",
//                    arguments = listOf(navArgument("placeId") { type = NavType.StringType })
//                ) { backStackEntry ->
//                    val placeId = backStackEntry.arguments?.getString("placeId")
//                        ?: return@composable
//
//                    DetailedCarScreen(
//
//                    )
//                }
                composable(Screen.Bookings.route) {
                    BookingsScreen()
                }
                composable(Screen.Settings.route) {
                    SettingsScreen()
                }
            }
        }
    }
}
