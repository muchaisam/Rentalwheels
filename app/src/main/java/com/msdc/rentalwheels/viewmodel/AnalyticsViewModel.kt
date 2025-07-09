package com.msdc.rentalwheels.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.msdc.rentalwheels.data.repository.BookingRepository
import com.msdc.rentalwheels.modules.IoDispatcher
import com.msdc.rentalwheels.uistates.BookingAnalytics
import com.msdc.rentalwheels.uistates.BookingStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel
@Inject
constructor(
    private val repository: BookingRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _analyticsState = MutableStateFlow<AnalyticsState>(AnalyticsState.Loading)
    val analyticsState: StateFlow<AnalyticsState> = _analyticsState.asStateFlow()

    sealed interface AnalyticsState {
        data object Loading : AnalyticsState
        data class Success(val analytics: BookingAnalytics) : AnalyticsState
        data class Error(val message: String) : AnalyticsState
    }

    fun loadAnalytics() {
        viewModelScope.launch(dispatcher) {
            try {
                _analyticsState.value = AnalyticsState.Loading

                val bookings = repository.getUserBookings().first()

                // Calculate analytics
                val completedBookings = bookings.filter { it.status == BookingStatus.COMPLETED }

                val totalBookings = bookings.size
                val totalSpent = completedBookings.sumOf { it.totalCost }

                val averageRentalDuration =
                    if (completedBookings.isNotEmpty()) {
                        completedBookings
                            .map { booking ->
                                java.time.Duration.between(
                                    booking.startDate,
                                    booking.endDate
                                )
                                    .toDays()
                            }
                            .average()
                            .toInt()
                    } else {
                        0
                    }

                val mostRentedCarBrand =
                    bookings.groupBy { it.car.make }.maxByOrNull { it.value.size }?.key ?: ""

                val preferredPickupLocation =
                    bookings.groupBy { it.pickupLocation }.maxByOrNull { it.value.size }?.key
                        ?: ""

                // Monthly spending (last 6 months)
                val monthlySpending = calculateMonthlySpending(completedBookings)

                // Favorite features based on car selections
                val favoriteFeatures = extractFavoriteFeatures(bookings)

                val analytics =
                    BookingAnalytics(
                        totalBookings = totalBookings,
                        totalSpent = totalSpent,
                        averageRentalDuration = averageRentalDuration,
                        mostRentedCarBrand = mostRentedCarBrand,
                        preferredPickupLocation = preferredPickupLocation,
                        monthlySpending = monthlySpending,
                        favoriteFeatures = favoriteFeatures
                    )

                _analyticsState.value = AnalyticsState.Success(analytics)
            } catch (e: Exception) {
                Timber.e(e, "Error loading analytics")
                _analyticsState.value =
                    AnalyticsState.Error(e.message ?: "Failed to load analytics")
            }
        }
    }

    private fun calculateMonthlySpending(
        completedBookings: List<com.msdc.rentalwheels.uistates.Booking>
    ): Map<String, Double> {
        val monthFormatter = DateTimeFormatter.ofPattern("MMM yyyy")

        return completedBookings
            .groupBy { booking -> booking.startDate.format(monthFormatter) }
            .mapValues { (_, bookings) -> bookings.sumOf { it.totalCost } }
            .toList()
            .sortedBy { (month, _) ->
                // Simple sort by month string - in production, you'd want proper date sorting
                month
            }
            .takeLast(6) // Last 6 months
            .toMap()
    }

    private fun extractFavoriteFeatures(
        bookings: List<com.msdc.rentalwheels.uistates.Booking>
    ): List<String> {
        val features = mutableListOf<String>()

        // Analyze car features from bookings
        bookings.forEach { booking ->
            val car = booking.car

            // Add features based on car attributes
            if (car.isElectric) features.add("Electric")
            if (car.hasGPS) features.add("GPS Navigation")
            if (car.hasAirConditioning) features.add("Air Conditioning")
            if (car.hasBluetoothConnectivity) features.add("Bluetooth")
            if (booking.withDriver) features.add("With Driver")

            // Add fuel type as feature
            features.add(car.fuelType)

            // Add car type/category
            features.add(car.type)
        }

        // Return top 5 most common features
        return features
            .groupBy { it }
            .mapValues { it.value.size }
            .toList()
            .sortedByDescending { it.second }
            .take(5)
            .map { it.first }
    }
}
