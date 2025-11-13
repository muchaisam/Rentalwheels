package com.msdc.rentalwheels.ui.components.bookings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.uistates.CartItem
import com.msdc.rentalwheels.utils.CurrencyUtils

@Composable
fun AvailableCarsSection(
    availableCars: List<Car>,
    cartItems: List<CartItem>,
    onAddToCart: (Car) -> Unit,
    onCarClick: (String) -> Unit,
    onViewCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
    ) {
        // Header with car count
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Available Cars",
                    style = RentalWheelsDesignSystem.Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text =
                    if (availableCars.isEmpty()) "No cars available"
                    else
                        "${availableCars.size} premium ${if (availableCars.size == 1) "vehicle" else "vehicles"} ready",
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }

            if (cartItems.isNotEmpty()) {
                CartButton(itemCount = cartItems.size, onViewCart = onViewCart)
            }
        }

        // Welcome message
        AnimatedVisibility(
            visible = true,
            enter = fadeIn(tween(300)) + slideInVertically(tween(300))
        ) { WelcomeCard() }

        // Available cars list or empty state
        if (availableCars.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                contentPadding =
                PaddingValues(vertical = RentalWheelsDesignSystem.Spacing.small),
                verticalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                itemsIndexed(items = availableCars, key = { _, car -> car.id }) { index, car ->
                    AnimatedVisibility(
                        visible = true,
                        enter =
                        fadeIn(tween(300, delayMillis = index * 50)) +
                                slideInVertically(
                                    tween(300, delayMillis = index * 50)
                                ) { it / 2 } +
                                scaleIn(
                                    tween(300, delayMillis = index * 50),
                                    initialScale = 0.9f
                                )
                    ) {
                        AvailableCarCard(
                            car = car,
                            isInCart = cartItems.any { it.car.id == car.id },
                            onAddToCart = { onAddToCart(car) },
                            onCarClick = { onCarClick(car.id) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier =
        modifier
            .fillMaxWidth()
            .padding(vertical = RentalWheelsDesignSystem.Spacing.Huge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(120.dp)
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = Icons.Default.SearchOff,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(56.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

        Text(
            text = "No Cars Available",
            style = RentalWheelsDesignSystem.Typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )

        Text(
            text = "Check back soon for new vehicles",
            style = RentalWheelsDesignSystem.Typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = RentalWheelsDesignSystem.Spacing.small)
        )
    }
}

@Composable
private fun WelcomeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.extraSmall
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                modifier = Modifier.size(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.medium))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome to RentalWheels!",
                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "Browse our premium collection and start your journey today.",
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                    modifier = Modifier.padding(top = RentalWheelsDesignSystem.Spacing.XXS)
                )
            }
        }
    }
}

@Composable
private fun CartButton(itemCount: Int, onViewCart: () -> Unit, modifier: Modifier = Modifier) {
    FilledTonalButton(
        onClick = onViewCart,
        modifier = modifier,
        colors =
        ButtonDefaults.filledTonalButtonColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        BadgedBox(
            badge = {
                if (itemCount > 0) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.error,
                        contentColor = MaterialTheme.colorScheme.onError
                    ) {
                        Text(
                            text = itemCount.toString(),
                            style = RentalWheelsDesignSystem.Typography.labelSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.small))
        Text(
            text = "Cart",
            style = RentalWheelsDesignSystem.Typography.labelLarge,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun AvailableCarCard(
    car: Car,
    isInCart: Boolean,
    onAddToCart: () -> Unit,
    onCarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onCarClick,
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.extraSmall
        )
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Large Car Image with gradient overlay
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)) {
                AsyncImage(
                    model =
                    ImageRequest.Builder(LocalContext.current)
                        .data(car.imageUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = "${car.make} ${car.model}",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )

                // Gradient overlay for better text contrast
                Box(
                    modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors =
                                listOf(
                                    Color.Transparent,
                                    Color.Black.copy(
                                        alpha = 0.7f
                                    )
                                ),
                                startY = 100f
                            )
                        )
                )

                // In Cart Badge (Top Right)
                if (isInCart) {
                    Surface(
                        modifier =
                        Modifier
                            .align(Alignment.TopEnd)
                            .padding(RentalWheelsDesignSystem.Spacing.medium),
                        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier =
                            Modifier.padding(
                                horizontal = RentalWheelsDesignSystem.Spacing.small,
                                vertical =
                                RentalWheelsDesignSystem.Spacing.extraSmall
                            ),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement =
                            Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.XXS)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "In Cart",
                                style = RentalWheelsDesignSystem.Typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }

                // Category Badge (Top Left)
                Surface(
                    modifier =
                    Modifier
                        .align(Alignment.TopStart)
                        .padding(RentalWheelsDesignSystem.Spacing.medium),
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
                    color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.9f)
                ) {
                    Row(
                        modifier =
                        Modifier.padding(
                            horizontal = RentalWheelsDesignSystem.Spacing.small,
                            vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                        ),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.XXS)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = car.category,
                            style = RentalWheelsDesignSystem.Typography.labelSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    }
                }

                // Car name and price overlay
                Column(
                    modifier =
                    Modifier
                        .align(Alignment.BottomStart)
                        .padding(RentalWheelsDesignSystem.Spacing.medium)
                ) {
                    Text(
                        text = "${car.make} ${car.model}",
                        style = RentalWheelsDesignSystem.Typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement =
                        Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.XXS)
                    ) {
                        Text(
                            text = CurrencyUtils.formatDailyRate(car.pricePerDay),
                            style = RentalWheelsDesignSystem.Typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "per day",
                            style = RentalWheelsDesignSystem.Typography.bodySmall,
                            color = Color.White.copy(alpha = 0.9f),
                            modifier = Modifier.padding(bottom = 2.dp)
                        )
                    }
                }
            }

            // Car Details and Actions
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                // Specs Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
                ) {
                    CarSpec(
                        icon = Icons.Default.Speed,
                        label = car.transmission,
                        modifier = Modifier.weight(1f)
                    )
                    CarSpec(
                        icon = Icons.Default.LocalGasStation,
                        label = car.fuelType,
                        modifier = Modifier.weight(1f)
                    )
                    CarSpec(
                        icon = Icons.Default.Settings,
                        label = "${car.seatingCapacity} Seats",
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
                ) {
                    OutlinedButton(
                        onClick = onCarClick,
                        modifier = Modifier.weight(1f),
                        colors =
                        ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(
                            text = "View Details",
                            style = RentalWheelsDesignSystem.Typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.weight(1f),
                        colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                            if (isInCart) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.primaryContainer,
                            contentColor =
                            if (isInCart)
                                MaterialTheme.colorScheme.onPrimary
                            else
                                MaterialTheme.colorScheme
                                    .onPrimaryContainer
                        )
                    ) {
                        Icon(
                            imageVector =
                            if (isInCart) Icons.Default.CheckCircle
                            else Icons.Default.Add,
                            contentDescription = if (isInCart) "Added" else "Add to cart",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.XXS))
                        Text(
                            text = if (isInCart) "Added" else "Add to Cart",
                            style = RentalWheelsDesignSystem.Typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CarSpec(icon: ImageVector, label: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(
                    horizontal = RentalWheelsDesignSystem.Spacing.small,
                    vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                ),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.XXS))
            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
