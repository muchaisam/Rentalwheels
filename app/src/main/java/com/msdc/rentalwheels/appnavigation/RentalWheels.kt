package com.msdc.rentalwheels.appnavigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.msdc.rentalwheels.ui.screens.AnalyticsScreen
import com.msdc.rentalwheels.ui.screens.BookingDetailScreen
import com.msdc.rentalwheels.ui.screens.BookingsScreen
import com.msdc.rentalwheels.ui.screens.BrowseScreen
import com.msdc.rentalwheels.ui.screens.HomeScreen
import com.msdc.rentalwheels.ui.screens.SettingsScreen
import com.msdc.rentalwheels.ui.theme.ThemeProvider
import com.msdc.rentalwheels.ui.theme.ThemeState
import com.msdc.rentalwheels.ui.theme.rememberThemeState
import com.msdc.rentalwheels.utils.CarDetailRoute
import com.msdc.rentalwheels.viewmodel.CarViewModel
import com.msdc.rentalwheels.viewmodel.SettingsViewModel

@Composable
fun RentalWheelsApp(
    viewModel: CarViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    themeState: ThemeState = rememberThemeState(),
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val carDetailState by viewModel.carDetailState.collectAsState()

    // Track current route to decide when to show bottom navigation
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Check if current screen should show bottom navigation
    val shouldShowBottomBar =
        when {
            currentRoute?.startsWith("car_detail/") == true -> false
            // Add other screens that should hide the bottom bar here
            else -> true
        }

    ThemeProvider(settingsViewModel = settingsViewModel) {
        Scaffold(
            bottomBar = {
                // Only show bottom navigation when shouldShowBottomBar is true
                if (shouldShowBottomBar) {
                    BottomNavigation(
                        currentDestination = navController.currentDestination,
                        onNavigate = { screen -> navController.navigate(screen.route) },
                        themeState = themeState
                    )
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(
                        viewModel = viewModel,
                        onCarClick = { carId ->
                            // Navigate to car detail screen
                            navController.navigate(Screen.DetailedCarScreen.createRoute(carId))
                        }
                    )
                }
                composable(
                    route = Screen.DetailedCarScreen.route,
                    arguments = listOf(navArgument("carId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val carId = backStackEntry.arguments?.getString("carId") ?: ""
                    val bookingsViewModel: com.msdc.rentalwheels.viewmodel.BookingsViewModel =
                        hiltViewModel()
                    CarDetailRoute(
                        carId = carId,
                        onBackClick = { navController.navigateUp() },
                        onBookNowClick = { car ->
                            // Create a quick booking and navigate to bookings screen
                            bookingsViewModel.createQuickBooking(
                                car = car,
                                onSuccess = {
                                    navController.navigate(Screen.Bookings.route)
                                },
                                onError = { error ->
                                    // Handle error - could show a toast or dialog
                                    // For now, just navigate to bookings anyway
                                    navController.navigate(Screen.Bookings.route)
                                }
                            )
                        }
                    )
                }
                composable(Screen.Browse.route) {
                    BrowseScreen(
                        onCarClick = { carId ->
                            navController.navigate(Screen.DetailedCarScreen.createRoute(carId))
                        }
                    )
                }

                composable(Screen.Bookings.route) {
                    BookingsScreen(
                        onBookingClick = { bookingId ->
                            navController.navigate(Screen.BookingDetail.createRoute(bookingId))
                        },
                        onNewBookingClick = { navController.navigate(Screen.Browse.route) },
                        onCarClick = { carId ->
                            navController.navigate(Screen.DetailedCarScreen.createRoute(carId))
                        },
                        onAnalyticsClick = { navController.navigate(Screen.Analytics.route) }
                    )
                }
                composable(
                    route = Screen.BookingDetail.route,
                    arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                    BookingDetailScreen(
                        bookingId = bookingId,
                        onBackClick = { navController.navigateUp() },
                        onCarClick = { carId ->
                            navController.navigate(Screen.DetailedCarScreen.createRoute(carId))
                        },
                        onCancelBooking = { bookingId ->
                            // Handle booking cancellation
                        },
                        onExtendBooking = { bookingId ->
                            // Handle booking extension
                        }
                    )
                }
                composable(Screen.Analytics.route) {
                    AnalyticsScreen(onBackClick = { navController.navigateUp() })
                }
                composable(Screen.Settings.route) { SettingsScreen(viewModel = settingsViewModel) }
            }
        }
    }
}
