package com.msdc.rentalwheels.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.FlightLand
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.components.PremiumButton
import com.msdc.rentalwheels.ui.components.PremiumButtonVariant
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.utils.CurrencyUtils
import com.msdc.rentalwheels.viewmodel.CarViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreen(onNavigateBack: () -> Unit, carViewModel: CarViewModel = hiltViewModel()) {
    val uiState by carViewModel.uiState.collectAsStateWithLifecycle()

    val cars =
        when (uiState) {
            is CarViewModel.UiState.Success -> (uiState as CarViewModel.UiState.Success).cars
            else -> emptyList()
        }

    val isLoading = uiState is CarViewModel.UiState.Loading

    var selectedCar by remember { mutableStateOf<Car?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var startDate by remember { mutableStateOf(LocalDate.now().plusDays(1)) }
    var endDate by remember { mutableStateOf(LocalDate.now().plusDays(4)) }
    var termsAccepted by remember { mutableStateOf(false) }
    var showTermsDialog by remember { mutableStateOf(false) }

    // Rental times - configurable (standard car rental pickup/return times)
    var pickupTime by remember { mutableStateOf("09:00 AM") }
    var dropoffTime by remember { mutableStateOf("09:00 AM") }

    val totalDays =
        remember(startDate, endDate) {
            ChronoUnit.DAYS.between(startDate, endDate).toInt().coerceAtLeast(1)
        }

    // Load initial data if needed
    LaunchedEffect(Unit) {
        if (uiState is CarViewModel.UiState.Loading) {
            carViewModel.loadInitialData()
        }
    }

    // Initialize with first available car
    LaunchedEffect(cars) {
        if (cars.isNotEmpty() && selectedCar == null) {
            selectedCar = cars.first()
        }
    }

    if (showDatePicker) {
        AnimatedDatePickerDialog(
            startDate = startDate,
            endDate = endDate,
            onDateRangeSelected = { start, end ->
                startDate = start
                endDate = end
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    if (showTermsDialog && selectedCar != null) {
        TermsAndConditionsDialog(car = selectedCar!!, onDismiss = { showTermsDialog = false })
    }

    Column(
        modifier =
        Modifier
            .fillMaxSize()
            .background(
                brush =
                Brush.verticalGradient(
                    colors =
                    listOf(
                        MaterialTheme.colorScheme
                            .background,
                        MaterialTheme.colorScheme
                            .surface
                    )
                )
            )
    ) {
        // Top Bar
        BookingTopBar(onNavigateBack = onNavigateBack)

        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else if (selectedCar != null) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(RentalWheelsDesignSystem.Spacing.medium),
                verticalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.large)
            ) {
                // Hero Car Image - Premium showcase
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { -it / 2 },
                            animationSpec = tween(500, easing = EaseOutCubic)
                        ) + fadeIn(tween(300))
                    ) { CarHeroCard(car = selectedCar!!) }
                }

                // Booking Dates - Prominent placement
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(600, easing = EaseOutCubic)
                        ) + fadeIn(tween(400))
                    ) {
                        Column(
                            verticalArrangement =
                            Arrangement.spacedBy(
                                RentalWheelsDesignSystem.Spacing.medium
                            )
                        ) {
                            BookingDatesCard(
                                startDate = startDate,
                                endDate = endDate,
                                totalDays = totalDays,
                                pickupTime = pickupTime,
                                dropoffTime = dropoffTime,
                                location = selectedCar?.location ?: "Location to be confirmed"
                            )

                            PremiumButton(
                                onClick = { showDatePicker = true },
                                modifier = Modifier.fillMaxWidth(),
                                variant = PremiumButtonVariant.Secondary
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = null,
                                        modifier =
                                        Modifier.size(
                                            RentalWheelsDesignSystem.Spacing.medium
                                        )
                                    )
                                    Spacer(
                                        modifier =
                                        Modifier.width(
                                            RentalWheelsDesignSystem.Spacing.small
                                        )
                                    )
                                    Text("Modify Rental Dates")
                                }
                            }
                        }
                    }
                }

                // Car Details - Show full specifications
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(700, easing = EaseOutCubic)
                        ) + fadeIn(tween(400))
                    ) { selectedCar?.let { CarDetailsCard(car = it) } }
                }

                // Pricing Breakdown - Enhanced visual
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(800, easing = EaseOutCubic)
                        ) + fadeIn(tween(400))
                    ) {
                        selectedCar?.let { car ->
                            PricingBreakdownCard(
                                dailyRate = car.dailyRate,
                                totalDays = totalDays,
                                subtotal = car.dailyRate * totalDays,
                                insurance = ((car.dailyRate * totalDays) * 0.15).toInt(),
                                taxes = ((car.dailyRate * totalDays) * 0.08).toInt(),
                                total = calculateTotalPrice(car.dailyRate, totalDays)
                            )
                        }
                    }
                }

                // Terms & Conditions - Compact version
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(900, easing = EaseOutCubic)
                        ) + fadeIn(tween(400))
                    ) {
                        selectedCar?.let { car ->
                            CompactTermsCard(car = car, onViewDetails = { showTermsDialog = true })
                        }
                    }
                }

                // Terms Acceptance
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(1000, easing = EaseOutCubic)
                        ) + fadeIn(tween(400))
                    ) {
                        TermsAcceptanceSection(
                            termsAccepted = termsAccepted,
                            onTermsToggle = { termsAccepted = it },
                            onViewTerms = { showTermsDialog = true }
                        )
                    }
                }

                // Action Buttons - Sticky feel
                item {
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        slideInVertically(
                            initialOffsetY = { it / 3 },
                            animationSpec = tween(1100, easing = EaseOutCubic)
                        ) + fadeIn(tween(400))
                    ) {
                        selectedCar?.let { car ->
                            BookingActionButtons(
                                car = car,
                                totalDays = totalDays,
                                termsAccepted = termsAccepted,
                                onBookNow = {
                                    // This is a mockup - no
                                    // actual booking
                                    // functionality
                                    // In a real app, this would
                                    // trigger booking process
                                }
                            )
                        }
                    }
                }

                // Bottom spacing for better UX
                item { Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.Huge)) }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BookingTopBar(onNavigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = "Complete Booking",
                style = RentalWheelsDesignSystem.Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors =
        TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    )
}

@Composable
private fun CarSelectionCard(cars: List<Car>, selectedCar: Car, onCarSelected: (Car) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Text(
                text = "Select Your Vehicle",
                style = RentalWheelsDesignSystem.Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = RentalWheelsDesignSystem.Spacing.medium)
            )

            LazyRow(
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium),
                contentPadding =
                PaddingValues(horizontal = RentalWheelsDesignSystem.Spacing.extraSmall)
            ) {
                items(cars) { car ->
                    val isSelected = car.id == selectedCar.id

                    AnimatedVisibility(
                        visible = true,
                        enter =
                        scaleIn(animationSpec = tween(400, easing = EaseOutBack)) +
                                fadeIn(tween(300))
                    ) {
                        SimpleCarCard(
                            car = car,
                            isSelected = isSelected,
                            onClick = { onCarSelected(car) },
                            modifier =
                            Modifier.width(
                                RentalWheelsDesignSystem.Spacing.Huge * 4.375f
                            ) // ~280dp
                        )
                    }
                }
            }

            if (cars.isNotEmpty()) {
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                Text(
                    text = "${cars.size} vehicles available for your dates",
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SimpleCarCard(
    car: Car,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier =
        modifier
            .background(
                color =
                if (isSelected)
                    MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surface,
                shape =
                RoundedCornerShape(
                    RentalWheelsDesignSystem.CornerRadius.medium
                )
            )
            .border(
                width =
                if (isSelected) RentalWheelsDesignSystem.Spacing.XXS / 2
                else RentalWheelsDesignSystem.Elevation.None,
                color =
                if (isSelected) MaterialTheme.colorScheme.primary
                else androidx.compose.ui.graphics.Color.Transparent,
                shape =
                RoundedCornerShape(
                    RentalWheelsDesignSystem.CornerRadius.medium
                )
            ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            // Car Image
            AsyncImage(
                model = car.imageUrl,
                contentDescription = "${car.make} ${car.model}",
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        RentalWheelsDesignSystem.Spacing.Huge * 1.875f
                    ) // ~120dp
                    .clip(
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius.small
                        )
                    ),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            // Car Info
            Text(
                text = "${car.make} ${car.model}",
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = CurrencyUtils.formatDailyRate(car.dailyRate),
                style = RentalWheelsDesignSystem.Typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun TermsAcceptanceSection(
    termsAccepted: Boolean,
    onTermsToggle: (Boolean) -> Unit,
    onViewTerms: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        colors =
        CardDefaults.cardColors(
            containerColor =
            if (termsAccepted) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.errorContainer
        ),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        border =
        BorderStroke(
            RentalWheelsDesignSystem.Spacing.XXS / 4,
            if (termsAccepted) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
            else MaterialTheme.colorScheme.error.copy(alpha = 0.3f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.Top) {
                Checkbox(
                    checked = termsAccepted,
                    onCheckedChange = onTermsToggle,
                    colors =
                    CheckboxDefaults.colors(
                        checkedColor = MaterialTheme.colorScheme.primary,
                        uncheckedColor =
                        if (termsAccepted) MaterialTheme.colorScheme.outline
                        else MaterialTheme.colorScheme.error
                    )
                )

                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Accept Terms & Conditions",
                        style = RentalWheelsDesignSystem.Typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

                    Text(
                        text =
                        "By proceeding, you confirm that you have read and agree to our rental agreement, cancellation policy, and insurance terms.",
                        style = RentalWheelsDesignSystem.Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                        lineHeight = RentalWheelsDesignSystem.Typography.bodyMedium.lineHeight
                    )

                    if (!termsAccepted) {
                        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.error,
                                modifier =
                                Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                            )
                            Spacer(
                                modifier =
                                Modifier.width(
                                    RentalWheelsDesignSystem.Spacing.extraSmall
                                )
                            )
                            Text(
                                text = "Required to complete booking",
                                style = RentalWheelsDesignSystem.Typography.bodySmall,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            PremiumButton(
                onClick = onViewTerms,
                modifier = Modifier.fillMaxWidth(),
                variant = PremiumButtonVariant.Secondary
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                    Text("Read Full Terms & Conditions")
                }
            }
        }
    }
}

@Composable
private fun BookingActionButtons(
    car: Car,
    totalDays: Int,
    termsAccepted: Boolean,
    onBookNow: () -> Unit
) {
    val totalPrice = calculateTotalPrice(car.dailyRate, totalDays)

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
    ) {
        // Total price display - More prominent
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
            colors =
            CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.medium)
        ) {
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(RentalWheelsDesignSystem.Spacing.large),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Total Booking Cost",
                        style = RentalWheelsDesignSystem.Typography.titleMedium,
                        color = Color.White.copy(alpha = 0.95f),
                        fontWeight = FontWeight.Medium
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = Color.White.copy(alpha = 0.8f),
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                        )
                        Spacer(
                            modifier =
                            Modifier.width(RentalWheelsDesignSystem.Spacing.extraSmall)
                        )
                        Text(
                            text = "$totalDays days rental",
                            style = RentalWheelsDesignSystem.Typography.bodyMedium,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = CurrencyUtils.formatToKSH(totalPrice),
                        style = RentalWheelsDesignSystem.Typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "All fees included",
                        style = RentalWheelsDesignSystem.Typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement =
            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            PremiumButton(
                onClick = {
                    // Mockup functionality - would save booking draft
                },
                modifier = Modifier.weight(1f),
                variant = PremiumButtonVariant.Secondary
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.BookmarkAdd,
                        contentDescription = null,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                    )
                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))
                    Text("Save Draft")
                }
            }

            PremiumButton(
                onClick = onBookNow,
                enabled = termsAccepted,
                modifier = Modifier.weight(1f)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                    )
                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))
                    Text("Book Now")
                }
            }
        }

        // Security and trust indicators
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
            colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(RentalWheelsDesignSystem.Spacing.medium),
                verticalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                SecurityFeature(icon = Icons.Default.Security, text = "256-bit SSL encryption")
                SecurityFeature(icon = Icons.Default.VerifiedUser, text = "Verified secure payment")
                SecurityFeature(icon = Icons.Default.MoneyOff, text = "No hidden charges")
            }
        }
    }
}

@Composable
private fun SecurityFeature(icon: ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
        )
        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
        Text(
            text = text,
            style = RentalWheelsDesignSystem.Typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TermsAndConditionsDialog(car: Car, onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.8f),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(RentalWheelsDesignSystem.Spacing.large),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Terms & Conditions",
                            style = RentalWheelsDesignSystem.Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )

                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }

                // Content
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(RentalWheelsDesignSystem.Spacing.large),
                    verticalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
                ) {
                    item {
                        TermsSection(
                            title = "Rental Agreement",
                            content =
                            "By booking this vehicle, you agree to our standard rental terms including proper vehicle usage, timely return, and damage responsibility."
                        )
                    }

                    item {
                        TermsSection(
                            title = "Cancellation Policy",
                            content = car.cancellationPolicy
                        )
                    }

                    item {
                        TermsSection(
                            title = "Insurance Coverage",
                            content =
                            if (car.insuranceIncluded)
                                "Comprehensive insurance coverage is included with this rental, covering collision, theft, and third-party liability."
                            else
                                "Insurance coverage is not included. You may purchase additional coverage or use your personal insurance policy."
                        )
                    }

                    if (car.depositRequired > 0) {
                        item {
                            TermsSection(
                                title = "Security Deposit",
                                content =
                                "A refundable security deposit of ${CurrencyUtils.formatToKSH(car.depositRequired.toInt())} will be held on your payment method during the rental period."
                            )
                        }
                    }

                    item {
                        TermsSection(
                            title = "Vehicle Return",
                            content =
                            "Vehicle must be returned clean, with the same fuel level as pickup, and without damage beyond normal wear and tear."
                        )
                    }
                }

                // Footer
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    PremiumButton(
                        onClick = onDismiss,
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(RentalWheelsDesignSystem.Spacing.large)
                    ) { Text("I Understand") }
                }
            }
        }
    }
}

@Composable
private fun TermsSection(title: String, content: String) {
    Column {
        Text(
            text = title,
            style = RentalWheelsDesignSystem.Typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))
        Text(
            text = content,
            style = RentalWheelsDesignSystem.Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = RentalWheelsDesignSystem.Typography.bodyMedium.lineHeight
        )
    }
}

private fun calculateTotalPrice(dailyRate: Int, totalDays: Int): Int {
    val subtotal = dailyRate * totalDays
    val insurance = (subtotal * 0.15).toInt()
    val taxes = (subtotal * 0.08).toInt()
    return subtotal + insurance + taxes
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AnimatedDatePickerDialog(
    startDate: LocalDate,
    endDate: LocalDate,
    onDateRangeSelected: (LocalDate, LocalDate) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedStartDate by remember { mutableStateOf(startDate) }
    var selectedEndDate by remember { mutableStateOf(endDate) }
    var isSelectingEndDate by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
            elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.large)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Header
                Surface(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(RentalWheelsDesignSystem.Spacing.large)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Select Dates",
                                style = RentalWheelsDesignSystem.Typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )

                            IconButton(onClick = onDismiss) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(
                                RentalWheelsDesignSystem.Spacing.medium
                            )
                        ) {
                            DateDisplayCard(
                                label = "Pick-up Date",
                                date = selectedStartDate,
                                isSelected = !isSelectingEndDate,
                                onClick = { isSelectingEndDate = false },
                                modifier = Modifier.weight(1f)
                            )

                            DateDisplayCard(
                                label = "Drop-off Date",
                                date = selectedEndDate,
                                isSelected = isSelectingEndDate,
                                onClick = { isSelectingEndDate = true },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                // Date Picker
                val datePickerState =
                    rememberDatePickerState(
                        initialSelectedDateMillis =
                        if (isSelectingEndDate) {
                            selectedEndDate.toEpochDay() * 24 * 60 * 60 * 1000
                        } else {
                            selectedStartDate.toEpochDay() * 24 * 60 * 60 * 1000
                        }
                    )

                LaunchedEffect(datePickerState.selectedDateMillis) {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val selectedDate = LocalDate.ofEpochDay(millis / (24 * 60 * 60 * 1000))
                        if (isSelectingEndDate) {
                            if (selectedDate.isAfter(selectedStartDate)) {
                                selectedEndDate = selectedDate
                            }
                        } else {
                            selectedStartDate = selectedDate
                            if (selectedEndDate.isBefore(selectedDate.plusDays(1))) {
                                selectedEndDate = selectedDate.plusDays(3)
                            }
                        }
                    }
                }

                DatePicker(state = datePickerState, modifier = Modifier.weight(1f))

                // Footer
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(RentalWheelsDesignSystem.Spacing.large)
                    ) {
                        val totalDays =
                            ChronoUnit.DAYS.between(selectedStartDate, selectedEndDate).toInt()

                        Text(
                            text = "Rental Duration: $totalDays days",
                            style = RentalWheelsDesignSystem.Typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                            Arrangement.spacedBy(
                                RentalWheelsDesignSystem.Spacing.medium
                            )
                        ) {
                            PremiumButton(
                                onClick = onDismiss,
                                modifier = Modifier.weight(1f),
                                variant = PremiumButtonVariant.Secondary
                            ) { Text("Cancel") }

                            PremiumButton(
                                onClick = {
                                    onDateRangeSelected(selectedStartDate, selectedEndDate)
                                },
                                modifier = Modifier.weight(1f)
                            ) { Text("Confirm Dates") }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DateDisplayCard(
    label: String,
    date: LocalDate,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color =
        if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        else MaterialTheme.colorScheme.surface,
        border =
        if (isSelected)
            BorderStroke(
                RentalWheelsDesignSystem.Spacing.XXS / 2,
                MaterialTheme.colorScheme.primary
            )
        else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelMedium,
                color =
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color =
                if (isSelected) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun BookingDatesCard(
    startDate: LocalDate,
    endDate: LocalDate,
    totalDays: Int,
    pickupTime: String,
    dropoffTime: String,
    location: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Header with gradient
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(RentalWheelsDesignSystem.Spacing.large),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Event,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                        )
                        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                        Text(
                            text = "Rental Period",
                            style = RentalWheelsDesignSystem.Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier =
                            Modifier.padding(
                                horizontal =
                                RentalWheelsDesignSystem.Spacing.medium,
                                vertical = RentalWheelsDesignSystem.Spacing.small
                            ),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Schedule,
                                contentDescription = null,
                                tint = Color.White,
                                modifier =
                                Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                            )
                            Spacer(
                                modifier =
                                Modifier.width(
                                    RentalWheelsDesignSystem.Spacing.extraSmall
                                )
                            )
                            Text(
                                text = "$totalDays Days",
                                style = RentalWheelsDesignSystem.Typography.labelLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Date cards
            Row(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(RentalWheelsDesignSystem.Spacing.large),
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                EnhancedDateCard(
                    icon = Icons.Default.FlightTakeoff,
                    label = "Pick-up",
                    date = startDate,
                    time = pickupTime,
                    isPrimary = true,
                    modifier = Modifier.weight(1f)
                )

                // Arrow between dates
                Box(
                    modifier = Modifier.align(Alignment.CenterVertically),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                    )
                }

                EnhancedDateCard(
                    icon = Icons.Default.FlightLand,
                    label = "Drop-off",
                    date = endDate,
                    time = dropoffTime,
                    isPrimary = false,
                    modifier = Modifier.weight(1f)
                )
            }

            // Location info
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(RentalWheelsDesignSystem.Spacing.large),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                    Column {
                        Text(
                            text = "Pick-up & Drop-off Location",
                            style = RentalWheelsDesignSystem.Typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                            fontWeight = FontWeight.Medium
                        )
                        Text(
                            text =
                            if (location.isNotBlank()) location
                            else "Location to be confirmed",
                            style = RentalWheelsDesignSystem.Typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EnhancedDateCard(
    icon: ImageVector,
    label: String,
    date: LocalDate,
    time: String,
    isPrimary: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color =
        if (isPrimary) MaterialTheme.colorScheme.primaryContainer
        else MaterialTheme.colorScheme.surfaceContainer,
        border =
        BorderStroke(
            RentalWheelsDesignSystem.Spacing.XXS / 4,
            if (isPrimary) MaterialTheme.colorScheme.primary
            else MaterialTheme.colorScheme.outlineVariant
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint =
                if (isPrimary) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge)
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )

            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMM dd, yyyy")),
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Surface(
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
                color =
                if (isPrimary) MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                else MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
            ) {
                Text(
                    text = time,
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    color =
                    if (isPrimary) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.secondary,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier =
                    Modifier.padding(
                        horizontal = RentalWheelsDesignSystem.Spacing.small,
                        vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                    )
                )
            }
        }
    }
}

@Composable
private fun DateCard(
    icon: ImageVector,
    label: String,
    date: LocalDate,
    time: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color = MaterialTheme.colorScheme.surfaceContainer
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Text(
                text = date.format(DateTimeFormatter.ofPattern("MMM dd")),
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = time,
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun PricingBreakdownCard(
    dailyRate: Int,
    totalDays: Int,
    subtotal: Int,
    insurance: Int,
    taxes: Int,
    total: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Receipt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                    Text(
                        text = "Price Breakdown",
                        style = RentalWheelsDesignSystem.Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Surface(
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
                    color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
                ) {
                    Text(
                        text = "$totalDays Days",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        modifier =
                        Modifier.padding(
                            horizontal = RentalWheelsDesignSystem.Spacing.medium,
                            vertical = RentalWheelsDesignSystem.Spacing.small
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

            // Daily Rate - Prominent
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(RentalWheelsDesignSystem.Spacing.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Daily Rate",
                            style = RentalWheelsDesignSystem.Typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "× $totalDays days rental",
                            style = RentalWheelsDesignSystem.Typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Text(
                        text = CurrencyUtils.formatToKSH(dailyRate),
                        style = RentalWheelsDesignSystem.Typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            PriceRow(label = "Rental Subtotal", value = CurrencyUtils.formatToKSH(subtotal))

            PriceRow(
                label = "Insurance Coverage",
                value = CurrencyUtils.formatToKSH(insurance),
                description = "Comprehensive protection (15%)"
            )

            PriceRow(
                label = "Taxes & Service Fees",
                value = CurrencyUtils.formatToKSH(taxes),
                description = "VAT & processing (8%)"
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            androidx.compose.material3.HorizontalDivider(
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                thickness = RentalWheelsDesignSystem.Spacing.XXS / 4
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            // Total - Bold and prominent
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Row(
                    modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(RentalWheelsDesignSystem.Spacing.medium),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Total Amount",
                            style = RentalWheelsDesignSystem.Typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "All fees included",
                            style = RentalWheelsDesignSystem.Typography.bodySmall,
                            color =
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                alpha = 0.8f
                            )
                        )
                    }

                    Text(
                        text = CurrencyUtils.formatToKSH(total),
                        style = RentalWheelsDesignSystem.Typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun PriceRow(label: String, value: String, description: String? = null) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = RentalWheelsDesignSystem.Spacing.extraSmall),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            description?.let {
                Text(
                    text = it,
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Text(
            text = value,
            style = RentalWheelsDesignSystem.Typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun CompactTermsCard(car: Car, onViewDetails: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                    Text(
                        text = "Rental Terms",
                        style = RentalWheelsDesignSystem.Typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                TextButton(onClick = onViewDetails) {
                    Text(
                        text = "View All",
                        style = RentalWheelsDesignSystem.Typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            // Quick highlights in chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                QuickTermChip(
                    icon = Icons.Default.Security,
                    text =
                    if (car.insuranceIncluded) "Insurance Included"
                    else "Insurance Available",
                    modifier = Modifier.weight(1f)
                )
                QuickTermChip(
                    icon = Icons.Default.Cancel,
                    text =
                    if (car.cancellationPolicy.contains("Free", ignoreCase = true))
                        "Free Cancellation"
                    else "Cancellation Policy",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                QuickTermChip(
                    icon = Icons.Default.LocalGasStation,
                    text = "Fuel Policy",
                    modifier = Modifier.weight(1f)
                )
                if (car.depositRequired > 0) {
                    QuickTermChip(
                        icon = Icons.Default.AccountBalance,
                        text =
                        "Deposit: ${CurrencyUtils.formatToKSH(car.depositRequired.toInt())}",
                        modifier = Modifier.weight(1f)
                    )
                } else {
                    QuickTermChip(
                        icon = Icons.Default.CheckCircle,
                        text = "No Deposit",
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun QuickTermChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        border =
        BorderStroke(
            RentalWheelsDesignSystem.Spacing.XXS / 4,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
        )
    ) {
        Row(
            modifier =
            Modifier.padding(
                horizontal = RentalWheelsDesignSystem.Spacing.medium,
                vertical = RentalWheelsDesignSystem.Spacing.small
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
            )
            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.extraSmall))
            Text(
                text = text,
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium,
                maxLines = 1
            )
        }
    }
}

@Composable
private fun TermsConditionsCard(car: Car) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                )
                Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
                Text(
                    text = "Important Information",
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            TermsItem(
                icon = Icons.Default.Security,
                title = "Insurance Coverage",
                description =
                if (car.insuranceIncluded) "Comprehensive insurance included"
                else "Insurance not included - additional coverage available"
            )

            TermsItem(
                icon = Icons.Default.AccessTime,
                title = "Cancellation Policy",
                description = car.cancellationPolicy
            )

            TermsItem(
                icon = Icons.Default.LocalGasStation,
                title = "Fuel Policy",
                description =
                "Full-to-full: Return vehicle with same fuel level as pickup. Refueling charges apply otherwise."
            )

            if (car.depositRequired > 0) {
                TermsItem(
                    icon = Icons.Default.Security,
                    title = "Security Deposit",
                    description =
                    "Refundable deposit of ${CurrencyUtils.formatToKSH(car.depositRequired.toInt())} required"
                )
            }
        }
    }
}

@Composable
private fun TermsItem(icon: ImageVector, title: String, description: String) {
    Row(
        modifier =
        Modifier
            .fillMaxWidth()
            .padding(vertical = RentalWheelsDesignSystem.Spacing.small),
        verticalAlignment = Alignment.Top
    ) {
        Surface(
            shape = androidx.compose.foundation.shape.CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge * 2) // ~32dp
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                )
            }
        }

        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.medium))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = RentalWheelsDesignSystem.Typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = RentalWheelsDesignSystem.Typography.bodyMedium.lineHeight
            )
        }
    }
}

@Composable
private fun CarHeroCard(car: Car) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.medium),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .height(RentalWheelsDesignSystem.Spacing.Huge * 4.375f)
        ) { // ~280dp
            // Car Image
            AsyncImage(
                model = car.imageUrl,
                contentDescription = "${car.make} ${car.model}",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.rental_logo)
            )

            // Gradient Overlay
            Box(
                modifier =
                Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors =
                            listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
                            ),
                            startY = 0f,
                            endY = Float.POSITIVE_INFINITY
                        )
                    )
            )

            // Car Info Overlay
            Column(
                modifier =
                Modifier
                    .align(Alignment.BottomStart)
                    .padding(RentalWheelsDesignSystem.Spacing.large)
            ) {
                Text(
                    text = "${car.make} ${car.model}",
                    style = RentalWheelsDesignSystem.Typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = car.year.toString(),
                    style = RentalWheelsDesignSystem.Typography.bodyLarge,
                    color = Color.White.copy(alpha = 0.9f)
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                // Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = Color(0xFFFBBF24),
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                    )
                    Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.extraSmall))
                    Text(
                        text = "${car.rating} (${car.reviewCount} reviews)",
                        style = RentalWheelsDesignSystem.Typography.bodySmall,
                        color = Color.White.copy(alpha = 0.9f)
                    )
                }
            }

            // Daily Rate Badge
            Surface(
                modifier =
                Modifier
                    .align(Alignment.TopEnd)
                    .padding(RentalWheelsDesignSystem.Spacing.medium),
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f)
            ) {
                Text(
                    text = CurrencyUtils.formatDailyRate(car.dailyRate),
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    modifier =
                    Modifier.padding(
                        horizontal = RentalWheelsDesignSystem.Spacing.medium,
                        vertical = RentalWheelsDesignSystem.Spacing.small
                    )
                )
            }
        }
    }
}

@Composable
private fun CarDetailsCard(car: Car) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation = CardDefaults.cardElevation(RentalWheelsDesignSystem.Elevation.small),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large)
        ) {
            Text(
                text = "Vehicle Specifications",
                style = RentalWheelsDesignSystem.Typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            // Specifications Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                SpecificationItem(
                    icon = Icons.Default.Speed,
                    label = "Transmission",
                    value = car.transmission,
                    modifier = Modifier.weight(1f)
                )
                SpecificationItem(
                    icon = Icons.Default.LocalGasStation,
                    label = "Fuel Type",
                    value = car.fuelType,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                SpecificationItem(
                    icon = Icons.Default.People,
                    label = "Seats",
                    value = "${car.seatingCapacity}",
                    modifier = Modifier.weight(1f)
                )
                SpecificationItem(
                    icon = Icons.Default.DirectionsBus,
                    label = "Type",
                    value = car.type,
                    modifier = Modifier.weight(1f)
                )
            }

            if (car.features.isNotEmpty()) {
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                Text(
                    text = "Features",
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

                car.features.take(6).chunked(2).forEach { featurePair ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
                    ) {
                        featurePair.forEach { feature ->
                            FeatureChip(text = feature, modifier = Modifier.weight(1f))
                        }
                        if (featurePair.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))
                }
            }
        }
    }
}

@Composable
private fun SpecificationItem(
    icon: ImageVector,
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.Spacing.small),
        color = MaterialTheme.colorScheme.surface,
        border =
        BorderStroke(
            RentalWheelsDesignSystem.Spacing.XXS / 4,
            MaterialTheme.colorScheme.outlineVariant
        ) // ~1dp
    ) {
        Column(
            modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge) // ~24dp
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))

            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )

            Text(
                text = value,
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun FeatureChip(text: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.Spacing.small),
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
        border =
        BorderStroke(
            RentalWheelsDesignSystem.Spacing.XXS / 4,
            MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
        ) // ~1dp
    ) {
        Text(
            text = text,
            style = RentalWheelsDesignSystem.Typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier =
            Modifier.padding(
                horizontal = RentalWheelsDesignSystem.Spacing.medium,
                vertical = RentalWheelsDesignSystem.Spacing.small
            ),
            textAlign = TextAlign.Center
        )
    }
}
