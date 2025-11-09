package com.msdc.rentalwheels.ui.components.bookings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.uistates.Booking
import com.msdc.rentalwheels.uistates.BookingStatus

@Composable
fun BookingsList(
        bookings: List<Booking>,
        onBookingClick: (String) -> Unit,
        onCarClick: (String) -> Unit,
        onCancelBooking: (String) -> Unit,
        onExtendBooking: (String) -> Unit,
        onRebookCar: (String) -> Unit,
        onToggleFavorite: (String) -> Unit = {},
        favoriteBookingIds: Set<String> = emptySet(),
        modifier: Modifier = Modifier,
        statusFilter: BookingStatus? = null,
        isLoading: Boolean = false
) {
    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading && bookings.isEmpty()) {
            CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.primary
            )
        } else if (bookings.isEmpty()) {
            EmptyBookingsState(
                    statusFilter = statusFilter,
                    modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding =
                            PaddingValues(vertical = RentalWheelsDesignSystem.Spacing.medium),
                    verticalArrangement =
                            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                items(bookings) { booking ->
                    BookingCard(
                            booking = booking,
                            onBookingClick = { onBookingClick(booking.id) },
                            onCarClick = { onCarClick(booking.carId) },
                            onCancelBooking = { onCancelBooking(booking.id) },
                            onExtendBooking = { onExtendBooking(booking.id) },
                            onRebookCar = { onRebookCar(booking.carId) },
                            onToggleFavorite = { onToggleFavorite(booking.id) },
                            isFavorite = favoriteBookingIds.contains(booking.id)
                    )
                }

                // Add spacing at the bottom for FAB
                item {
                    Spacer(
                            modifier =
                                    Modifier.height(RentalWheelsDesignSystem.Spacing.Huge * 1.25f)
                    )
                } // ~80dp
            }
        }
    }
}

@Composable
private fun EmptyBookingsState(statusFilter: BookingStatus?, modifier: Modifier = Modifier) {
    Column(
            modifier = modifier.padding(RentalWheelsDesignSystem.Spacing.extraLarge * 1.33f),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val message =
                when (statusFilter) {
                    BookingStatus.CONFIRMED -> "No upcoming bookings"
                    BookingStatus.ACTIVE -> "No active rentals"
                    BookingStatus.COMPLETED -> "No past bookings"
                    BookingStatus.CANCELLED -> "No cancelled bookings"
                    else -> "No bookings yet"
                }

        val subtitle =
                when (statusFilter) {
                    BookingStatus.CONFIRMED -> "Ready to plan your next adventure?"
                    BookingStatus.ACTIVE -> "All rentals completed"
                    BookingStatus.COMPLETED -> "Your rental history will appear here"
                    BookingStatus.CANCELLED -> "All your bookings are active"
                    else -> "Browse available cars to start your journey"
                }

        Text(
                text = message,
                style = RentalWheelsDesignSystem.Typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

        Text(
                text = subtitle,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
    }
}
