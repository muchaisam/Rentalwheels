package com.msdc.rentalwheels.appnavigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Browse : Screen("search")
    object DetailedCarScreen : Screen("car_detail/{carId}") {
        fun createRoute(carId: String) = "car_detail/$carId"
    }
    object Bookings : Screen("library")
    object Settings : Screen("settings")
}