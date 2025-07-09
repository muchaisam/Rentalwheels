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
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
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
        verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Start your first rental",
                    style = MaterialTheme.typography.bodyMedium,
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
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
            MaterialTheme.colorScheme.primaryContainer.copy(
                alpha = 0.7f
            )
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "🚗 Welcome to RentalWheels!",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                textAlign = TextAlign.Center
            )
            Text(
                text =
                "Browse our amazing collection of vehicles and start your journey.",
                style = MaterialTheme.typography.bodyMedium,
                color =
                MaterialTheme.colorScheme.onPrimaryContainer.copy(
                    alpha = 0.8f
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
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
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = "Cart ($itemCount)",
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier.padding(start = 4.dp)
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
            containerColor =
            MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Car Image
            Surface(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(12.dp)),
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
            Column(modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp)) {
                Text(
                    text = "${car.make} ${car.model}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = car.category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = CurrencyUtils.formatDailyRate(car.pricePerDay),
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Action Buttons
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(
                    onClick = onCarClick,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                    ButtonDefaults.outlinedButtonColors(
                        contentColor =
                        MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "View",
                        style = MaterialTheme.typography.labelMedium
                    )
                }

                Button(
                    onClick = onAddToCart,
                    colors =
                    ButtonDefaults.buttonColors(
                        containerColor =
                        if (isInCart)
                            MaterialTheme.colorScheme
                                .primary
                        else
                            MaterialTheme.colorScheme
                                .primaryContainer,
                        contentColor =
                        if (isInCart)
                            MaterialTheme.colorScheme
                                .onPrimary
                        else
                            MaterialTheme.colorScheme
                                .onPrimaryContainer
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription =
                        if (isInCart) "Added" else "Add to cart",
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = if (isInCart) "Added" else "Add",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}
