package com.msdc.rentalwheels.ui.components.bookings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.uistates.CartItem
import java.time.format.DateTimeFormatter

@Composable
fun CartView(
    cartItems: List<CartItem>,
    cartTotal: Double,
    onRemoveFromCart: (String) -> Unit,
    onEditCartItem: (CartItem) -> Unit,
    onProcessCart: () -> Unit,
    onClearCart: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
    ) {
        // Cart Header
        CartHeader(itemCount = cartItems.size, onClearCart = onClearCart)

        if (cartItems.isEmpty()) {
            EmptyCartView()
        } else {
            // Cart Items
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding =
                PaddingValues(vertical = RentalWheelsDesignSystem.Spacing.small),
                verticalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                items(items = cartItems, key = { it.car.id }) { cartItem ->
                    CartItemCard(
                        cartItem = cartItem,
                        onRemove = { onRemoveFromCart(cartItem.car.id) },
                        onEdit = { onEditCartItem(cartItem) }
                    )
                }
            }

            // Cart Summary
            CartSummary(total = cartTotal, onProcessCart = onProcessCart)
        }
    }
}

@Composable
private fun CartHeader(itemCount: Int, onClearCart: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Cart",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.extraLarge)
            )
            Column {
                Text(
                    text = "Your Cart",
                    style = RentalWheelsDesignSystem.Typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "$itemCount ${if (itemCount == 1) "item" else "items"}",
                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        if (itemCount > 0) {
            OutlinedButton(
                onClick = onClearCart,
                colors =
                ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) { Text("Clear All") }
        }
    }
}

@Composable
private fun EmptyCartView(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Empty cart",
                modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.Huge),
                tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
            )
            Text(
                text = "Your cart is empty",
                style = RentalWheelsDesignSystem.Typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Add some cars to get started",
                style = RentalWheelsDesignSystem.Typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun CartItemCard(
    cartItem: CartItem,
    onRemove: () -> Unit,
    onEdit: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
        ),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Car Image
                Surface(
                    modifier =
                    Modifier
                        .size(RentalWheelsDesignSystem.Spacing.Huge * 1.25f)
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
                            .data(cartItem.car.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = "${cartItem.car.make} ${cartItem.car.model}",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }

                // Car Details
                Column(
                    modifier =
                    Modifier
                        .weight(1f)
                        .padding(
                            horizontal = RentalWheelsDesignSystem.Spacing.medium
                        )
                ) {
                    Text(
                        text = "${cartItem.car.make} ${cartItem.car.model}",
                        style = RentalWheelsDesignSystem.Typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = cartItem.car.category,
                        style = RentalWheelsDesignSystem.Typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "$${cartItem.car.pricePerDay}/day",
                        style = RentalWheelsDesignSystem.Typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Action buttons
                Column(
                    verticalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.extraSmall)
                ) {
                    IconButton(
                        onClick = onEdit,
                        modifier =
                        Modifier.size(
                            RentalWheelsDesignSystem.Spacing.extraLarge * 1.67f
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                        )
                    }
                    IconButton(
                        onClick = onRemove,
                        modifier =
                        Modifier.size(
                            RentalWheelsDesignSystem.Spacing.extraLarge * 1.67f
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Remove",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                        )
                    }
                }
            }

            // Rental Details
            HorizontalDivider(
                modifier = Modifier.padding(vertical = RentalWheelsDesignSystem.Spacing.small),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            RentalDetails(cartItem = cartItem)
        }
    }
}

@Composable
private fun RentalDetails(cartItem: CartItem, modifier: Modifier = Modifier) {
    val dateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Pick-up:",
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = cartItem.startDate.format(dateFormatter),
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Return:",
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = cartItem.endDate.format(dateFormatter),
                style = RentalWheelsDesignSystem.Typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        if (cartItem.withDriver) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "With Driver:",
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Yes",
                    style = RentalWheelsDesignSystem.Typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "Total:",
                style = RentalWheelsDesignSystem.Typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "$${String.format("%.2f", cartItem.totalCost)}",
                style = RentalWheelsDesignSystem.Typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun CartSummary(total: Double, onProcessCart: () -> Unit, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors =
        CardDefaults.cardColors(
            containerColor =
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
        ),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(RentalWheelsDesignSystem.Spacing.large),
            verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total Amount:",
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                    text = "$${String.format("%.2f", total)}",
                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Button(
                onClick = onProcessCart,
                modifier = Modifier.fillMaxWidth(),
                colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Proceed to Booking",
                    style = RentalWheelsDesignSystem.Typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
