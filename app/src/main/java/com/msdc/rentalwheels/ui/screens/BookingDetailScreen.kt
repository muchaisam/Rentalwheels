package com.msdc.rentalwheels.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.msdc.rentalwheels.ui.components.ErrorScreen
import com.msdc.rentalwheels.ui.components.LoadingScreen
import com.msdc.rentalwheels.uistates.Booking
import com.msdc.rentalwheels.uistates.BookingStatus
import com.msdc.rentalwheels.uistates.PaymentStatus
import com.msdc.rentalwheels.viewmodel.BookingDetailViewModel
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingDetailScreen(
    bookingId: String,
    onBackClick: () -> Unit,
    onCarClick: (String) -> Unit,
    onCancelBooking: (String) -> Unit,
    onExtendBooking: (String) -> Unit,
    onCallDriver: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: BookingDetailViewModel = hiltViewModel()
) {
    val bookingState by viewModel.bookingState.collectAsState()
    var showCancelDialog by remember { mutableStateOf(false) }

    LaunchedEffect(bookingId) { viewModel.loadBooking(bookingId) }

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Booking Details",
                            style =
                            MaterialTheme.typography
                                .headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector =
                                Icons.Default.ArrowBack,
                                contentDescription = "Back"
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { /* Share booking details */ }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Share,
                                contentDescription = "Share"
                            )
                        }
                    },
                    colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent
                    )
                )
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            when (val state = bookingState) {
                is BookingDetailViewModel.BookingDetailState.Loading -> {
                    LoadingScreen()
                }

                is BookingDetailViewModel.BookingDetailState.Error -> {
                    ErrorScreen(
                        message = state.message,
                        onRetry = { viewModel.loadBooking(bookingId) }
                    )
                }

                is BookingDetailViewModel.BookingDetailState.Success -> {
                    BookingDetailContent(
                        booking = state.booking,
                        onCarClick = onCarClick,
                        onCancelClick = { showCancelDialog = true },
                        onExtendClick = { onExtendBooking(bookingId) },
                        onCallDriver = onCallDriver,
                        modifier = Modifier.padding(paddingValues)
                    )
                }
            }
        }
    }
}

@Composable
private fun BookingDetailContent(
    booking: Booking,
    onCarClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    onExtendClick: () -> Unit,
    onCallDriver: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier =
        modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Status Banner
        BookingStatusBanner(booking = booking)

        // Car Information
        CarInformationCard(booking = booking, onCarClick = { onCarClick(booking.carId) })

        // Booking Details
        BookingDetailsCard(booking = booking)

        // Driver Information (if applicable)
        booking.driverInfo?.let { driver ->
            DriverInformationCard(
                driver = driver,
                onCallDriver = { onCallDriver(driver.phoneNumber) }
            )
        }

        // Payment Information
        PaymentInformationCard(booking = booking)

        // Action Buttons
        ActionButtonsSection(
            booking = booking,
            onCancelClick = onCancelClick,
            onExtendClick = onExtendClick
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun BookingStatusBanner(booking: Booking) {
    val statusColor = Color(android.graphics.Color.parseColor(booking.status.colorHex))

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = statusColor.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .size(12.dp)
                .background(statusColor, CircleShape))

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = booking.status.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = statusColor
                )
                Text(
                    text = "Reference: ${booking.referenceNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun CarInformationCard(booking: Booking, onCarClick: () -> Unit) {
    Card(
        onClick = onCarClick,
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = booking.car.imageUrl,
                contentDescription = "${booking.car.make} ${booking.car.model}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${booking.car.make} ${booking.car.model}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${booking.car.year} • ${booking.car.fuelType}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$${booking.car.pricePerDay}/day",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Icon(
                imageVector = Icons.Default.DirectionsCar,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun BookingDetailsCard(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Booking Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            DetailRow(
                icon = Icons.Default.CalendarToday,
                label = "Start Date",
                value =
                booking.startDate.format(
                    DateTimeFormatter.ofPattern(
                        "MMM dd, yyyy 'at' h:mm a"
                    )
                )
            )

            DetailRow(
                icon = Icons.Default.Schedule,
                label = "End Date",
                value =
                booking.endDate.format(
                    DateTimeFormatter.ofPattern(
                        "MMM dd, yyyy 'at' h:mm a"
                    )
                )
            )

            DetailRow(
                icon = Icons.Default.LocationOn,
                label = "Pickup Location",
                value = booking.pickupLocation
            )

            DetailRow(
                icon = Icons.Default.LocationOn,
                label = "Return Location",
                value = booking.returnLocation
            )

            if (booking.specialRequests.isNotBlank()) {
                DetailRow(
                    icon = Icons.Default.Person,
                    label = "Special Requests",
                    value = booking.specialRequests
                )
            }
        }
    }
}

@Composable
private fun DriverInformationCard(
    driver: com.msdc.rentalwheels.uistates.DriverInfo,
    onCallDriver: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Driver Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = driver.imageUrl,
                    contentDescription = driver.name,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = driver.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${driver.experience} experience",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Rating: ${driver.rating}/5.0",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                IconButton(onClick = onCallDriver) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Call Driver",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun PaymentInformationCard(booking: Booking) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Payment Information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total Amount:",
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "$${booking.totalCost}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Payment Status:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = booking.paymentStatus.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    color =
                    when (booking.paymentStatus) {
                        PaymentStatus.PAID -> Color(0xFF4CAF50)
                        PaymentStatus.PENDING -> Color(0xFFFF9800)
                        PaymentStatus.PARTIAL -> Color(0xFFFF9800)
                        PaymentStatus.FAILED -> Color(0xFFF44336)
                        PaymentStatus.REFUNDED -> Color(0xFF9C27B0)
                    }
                )
            }
        }
    }
}

@Composable
private fun ActionButtonsSection(
    booking: Booking,
    onCancelClick: () -> Unit,
    onExtendClick: () -> Unit
) {
    val canCancel = booking.status in listOf(BookingStatus.CONFIRMED, BookingStatus.PENDING)
    val canExtend = booking.status == BookingStatus.ACTIVE

    if (canCancel || canExtend) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (canCancel) {
                OutlinedButton(
                    onClick = onCancelClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel")
                }
            }

            if (canExtend) {
                Button(onClick = onExtendClick, modifier = Modifier.weight(1f)) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Extend")
                }
            }
        }
    }
}

@Composable
private fun DetailRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
