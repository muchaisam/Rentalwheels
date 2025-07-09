package com.msdc.rentalwheels.ui.components.bookings

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.ExtensionOff
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msdc.rentalwheels.data.model.brand
import com.msdc.rentalwheels.uistates.Booking
import com.msdc.rentalwheels.uistates.BookingStatus
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@Composable
fun BookingCard(
    booking: Booking,
    onBookingClick: () -> Unit,
    onCarClick: () -> Unit,
    onCancelBooking: () -> Unit,
    onExtendBooking: () -> Unit,
    onRebookCar: () -> Unit,
    onToggleFavorite: () -> Unit = {},
    isFavorite: Boolean = false,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    val statusColor by
    animateColorAsState(
        targetValue = Color(android.graphics.Color.parseColor(booking.status.colorHex)),
        label = "status_color"
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onBookingClick() },
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Status Badge
                Box(
                    modifier =
                    Modifier
                        .background(
                            color = statusColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier =
                            Modifier
                                .size(8.dp)
                                .background(
                                    color = statusColor,
                                    shape = CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = booking.status.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Medium,
                            color = statusColor
                        )
                    }
                } // Actions Row
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Favorite Button
                    IconButton(onClick = onToggleFavorite) {
                        Icon(
                            imageVector =
                            if (isFavorite) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription =
                            if (isFavorite) "Remove from favorites"
                            else "Add to favorites",
                            tint =
                            if (isFavorite) MaterialTheme.colorScheme.error
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // More Options Menu
                    Box {
                        IconButton(onClick = { showMenu = true }) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options"
                            )
                        }

                        DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                            // Show different options based on booking status
                            when (booking.status) {
                                BookingStatus.CONFIRMED -> {
                                    DropdownMenuItem(
                                        text = { Text("Cancel Booking") },
                                        onClick = {
                                            showMenu = false
                                            onCancelBooking()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Cancel,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }

                                BookingStatus.ACTIVE -> {
                                    DropdownMenuItem(
                                        text = { Text("Extend Rental") },
                                        onClick = {
                                            showMenu = false
                                            onExtendBooking()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.ExtensionOff,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }

                                BookingStatus.COMPLETED -> {
                                    DropdownMenuItem(
                                        text = { Text("Book Again") },
                                        onClick = {
                                            showMenu = false
                                            onRebookCar()
                                        },
                                        leadingIcon = {
                                            Icon(
                                                Icons.Default.Refresh,
                                                contentDescription = null
                                            )
                                        }
                                    )
                                }

                                else -> {
                                    // No actions for other statuses
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Car Information Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Car Image
                    AsyncImage(
                        model =
                        booking.car.imageUrl.ifEmpty {
                            "https://via.placeholder.com/100x60?text=${booking.car.brand}"
                        },
                        contentDescription = "${booking.car.brand} ${booking.car.model}",
                        modifier =
                        Modifier
                            .size(80.dp, 50.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { onCarClick() },
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    // Car Details
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${booking.car.brand} ${booking.car.model}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )

                        Text(
                            text = booking.car.category,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )

                        Text(
                            text = "$${booking.totalCost.toInt()} total",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Booking Details
                BookingDetailRow(
                    icon = Icons.Default.DateRange,
                    label = "Duration",
                    value = formatBookingDuration(booking)
                )

                Spacer(modifier = Modifier.height(8.dp))

                BookingDetailRow(
                    icon = Icons.Default.LocationOn,
                    label = "Pickup",
                    value = booking.pickupLocation
                )

                if (booking.returnLocation != booking.pickupLocation) {
                    Spacer(modifier = Modifier.height(8.dp))
                    BookingDetailRow(
                        icon = Icons.Default.LocationOn,
                        label = "Return",
                        value = booking.returnLocation
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                BookingDetailRow(
                    icon = Icons.Default.AccessTime,
                    label = "Reference",
                    value = "#${booking.referenceNumber.ifEmpty { booking.id.take(8) }}"
                )

                // Driver Information (if applicable)
                if (booking.withDriver && booking.driverInfo != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    DriverInfoSection(driverInfo = booking.driverInfo)
                }
            }
        }
    }
}

@Composable
private fun BookingDetailRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            modifier = Modifier.size(16.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun DriverInfoSection(
    driverInfo: com.msdc.rentalwheels.uistates.DriverInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model =
                driverInfo.imageUrl.ifEmpty {
                    "https://via.placeholder.com/40x40?text=${driverInfo.name.firstOrNull()}"
                },
                contentDescription = driverInfo.name,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column {
                Text(
                    text = "Driver: ${driverInfo.name}",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "★ ${driverInfo.rating} • ${driverInfo.experience}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
        }
    }
}

private fun formatBookingDuration(booking: Booking): String {
    val formatter = DateTimeFormatter.ofPattern("MMM dd, HH:mm")
    val days = ChronoUnit.DAYS.between(booking.startDate, booking.endDate)

    return if (days == 0L) {
        "${booking.startDate.format(formatter)} - ${
            booking.endDate.format(
                DateTimeFormatter.ofPattern(
                    "HH:mm"
                )
            )
        }"
    } else {
        "${booking.startDate.format(formatter)} - ${booking.endDate.format(formatter)} (${days + 1} day${if (days > 0) "s" else ""})"
    }
}
