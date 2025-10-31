package com.msdc.rentalwheels.ui.components.bookings

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        // Header
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
                    text = "Start your first rental",
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (cartItems.isNotEmpty()) {
                CartButton(itemCount = cartItems.size, onViewCart = onViewCart)
            }
        }

        // Welcome message
        WelcomeCard()

        // Available cars list
        LazyColumn(
            contentPadding = PaddingValues(vertical = RentalWheelsDesignSystem.Spacing.small),
            verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            items(items = availableCars, key = { it.id }) { car ->
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

@Composable
private fun WelcomeCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🚗 Welcome to RentalWheels!",
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Browse our amazing collection of vehicles and start your journey.",
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = RentalWheelsDesignSystem.Spacing.small)
            )
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
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Cart",
            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
        )
        Text(
            text = "Cart ($itemCount)",
            style = RentalWheelsDesignSystem.Typography.labelMedium,
            modifier = Modifier.padding(start = RentalWheelsDesignSystem.Spacing.extraSmall)
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
    val buttonColor by
    animateColorAsState(
        targetValue =
        if (isInCart) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.outline,
        animationSpec = tween(300),
        label = "button_color"
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Car Image
            Surface(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge * 1.25f) // ~80dp
                    .clip(
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius.medium
                        )
                    ),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            ) {
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
            }

            // Car Details
            Column(
                modifier =
                Modifier
                    .weight(1f)
                    .padding(horizontal = RentalWheelsDesignSystem.Spacing.medium)
            ) {
                Text(
                    text = "${car.make} ${car.model}",
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = car.category,
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = CurrencyUtils.formatDailyRate(car.pricePerDay),
                    style = RentalWheelsDesignSystem.Typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier =
                    Modifier.padding(top = RentalWheelsDesignSystem.Spacing.extraSmall)
                )
            }

            // Action Buttons
            Column(
                verticalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                OutlinedButton(
                    onClick = onCarClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) { Text(text = "View", style = RentalWheelsDesignSystem.Typography.labelMedium) }

                Button(
                    onClick = onAddToCart,
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                        if (isInCart) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.primaryContainer,
                        contentColor =
                        if (isInCart) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = if (isInCart) "Added" else "Add to cart",
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium)
                    )
                    Text(
                        text = if (isInCart) "Added" else "Add",
                        style = RentalWheelsDesignSystem.Typography.labelMedium,
                        modifier =
                        Modifier.padding(
                            start = RentalWheelsDesignSystem.Spacing.extraSmall
                        )
                    )
                }
            }
        }
    }
}
