package com.msdc.rentalwheels.appnavigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Browse : Screen("search")
    object DetailedCarScreen : Screen("car_detail/{carId}") {
        fun createRoute(carId: String) = "car_detail/$carId"
    }

    object Bookings : Screen("library")
    object BookingDetail : Screen("booking_detail/{bookingId}") {
        fun createRoute(bookingId: String) = "booking_detail/$bookingId"
    }

    object Analytics : Screen("analytics")
    object Settings : Screen("settings")
}
