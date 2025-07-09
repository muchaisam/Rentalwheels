package com.msdc.rentalwheels.uistates

import com.msdc.rentalwheels.data.model.Car
import java.time.LocalDateTime

data class Booking(
    val id: String = "",
    val carId: String = "",
    val car: Car = Car(),
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val startDate: LocalDateTime = LocalDateTime.now(),
    val endDate: LocalDateTime = LocalDateTime.now().plusDays(1),
    val pickupLocation: String = "",
    val returnLocation: String = "",
    val totalCost: Double = 0.0,
    val paymentStatus: PaymentStatus = PaymentStatus.PENDING,
    val withDriver: Boolean = false,
    val driverInfo: DriverInfo? = null,
    val specialRequests: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    val referenceNumber: String = "",
    val customerNotes: String = ""
)

data class DriverInfo(
    val id: String = "",
    val name: String = "",
    val phoneNumber: String = "",
    val rating: Float = 0f,
    val imageUrl: String = "",
    val experience: String = ""
)

enum class BookingStatus(val displayName: String, val colorHex: String) {
    PENDING("Pending Confirmation", "#FFA726"),
    CONFIRMED("Confirmed", "#66BB6A"),
    ACTIVE("Active", "#42A5F5"),
    COMPLETED("Completed", "#8E8E93"),
    CANCELLED("Cancelled", "#EF5350"),
    MODIFIED("Modified", "#AB47BC"),
    EXPIRED("Expired", "#FF7043")
}

enum class PaymentStatus(val displayName: String) {
    PENDING("Payment Pending"),
    PARTIAL("Partially Paid"),
    PAID("Fully Paid"),
    REFUNDED("Refunded"),
    FAILED("Payment Failed")
}

sealed interface BookingsScreenState {
    data object Loading : BookingsScreenState

    data class Success(
        val allBookings: List<Booking>,
        val upcomingBookings: List<Booking>,
        val activeBookings: List<Booking>,
        val pastBookings: List<Booking>,
        val cancelledBookings: List<Booking>,
        val availableCars: List<Car> = emptyList(),
        val cartItems: List<CartItem> = emptyList(),
        val isRefreshing: Boolean = false,
        val selectedFilter: BookingFilter = BookingFilter.ALL,
        val searchQuery: String = "",
        val totalSpent: Double = 0.0,
        val favoriteCarIds: Set<String> = emptySet(),
        val hasBookings: Boolean = true,
        val showCartMode: Boolean = false
    ) : BookingsScreenState

    data class Error(val message: String) : BookingsScreenState
}

data class CartItem(
    val car: Car,
    val startDate: LocalDateTime = LocalDateTime.now().plusDays(1),
    val endDate: LocalDateTime = LocalDateTime.now().plusDays(2),
    val pickupLocation: String = "",
    val withDriver: Boolean = false,
    val quantity: Int = 1
) {
    val totalCost: Double
        get() {
            val days =
                java.time.temporal.ChronoUnit.DAYS.between(
                    startDate.toLocalDate(),
                    endDate.toLocalDate()
                )
            val rentalDays = if (days < 1) 1 else days.toInt()
            return car.pricePerDay * rentalDays * quantity
        }
}

enum class BookingFilter(val displayName: String) {
    ALL("All Bookings"),
    UPCOMING("Upcoming"),
    ACTIVE("Active"),
    PAST("Past"),
    CANCELLED("Cancelled")
}

// Analytics data for insights
data class BookingAnalytics(
    val totalBookings: Int = 0,
    val totalSpent: Double = 0.0,
    val averageRentalDuration: Int = 0, // in days
    val mostRentedCarBrand: String = "",
    val preferredPickupLocation: String = "",
    val monthlySpending: Map<String, Double> = emptyMap(),
    val favoriteFeatures: List<String> = emptyList()
)
