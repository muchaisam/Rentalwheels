package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.utils.CurrencyUtils

enum class CarCardVariant {
    Standard, // Full card with all details
    Compact, // Smaller card for grids
    Featured // Hero card with enhanced styling
}

/** Premium Car Card Component Professional design with micro-animations and proper branding */
@Composable
fun PremiumCarCard(
        car: Car,
        onClick: () -> Unit,
        modifier: Modifier = Modifier,
        variant: CarCardVariant = CarCardVariant.Standard,
        onFavorite: (() -> Unit)? = null,
        showPricing: Boolean = true,
        isFavorite: Boolean = false
) {
    val (cardWidth, cardHeight, imageHeight) =
            when (variant) {
                CarCardVariant.Standard ->
                        Triple(
                                RentalWheelsDesignSystem.Spacing.Huge * 4.375f,
                                RentalWheelsDesignSystem.Spacing.Huge * 5.625f,
                                RentalWheelsDesignSystem.Spacing.Huge * 3.125f
                        )
                CarCardVariant.Compact ->
                        Triple(
                                RentalWheelsDesignSystem.Spacing.Huge * 3.125f,
                                RentalWheelsDesignSystem.Spacing.Huge * 4.375f,
                                RentalWheelsDesignSystem.Spacing.Huge * 2.1875f
                        )
                CarCardVariant.Featured ->
                        Triple(
                                RentalWheelsDesignSystem.Spacing.Huge * 5f,
                                RentalWheelsDesignSystem.Spacing.Huge * 6.25f,
                                RentalWheelsDesignSystem.Spacing.Huge * 3.75f
                        )
            }

    Card(
            modifier =
                    modifier.width(cardWidth)
                            .height(cardHeight)
                            .animateContentSize(
                                    animationSpec =
                                            spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                            )
                            )
                            .clickable { onClick() },
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation =
                    CardDefaults.cardElevation(
                            defaultElevation = RentalWheelsDesignSystem.Elevation.Card,
                            pressedElevation = RentalWheelsDesignSystem.Elevation.large,
                            hoveredElevation = RentalWheelsDesignSystem.Elevation.large
                    )
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Hero Image Section
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(imageHeight)
                                        .clip(
                                                RoundedCornerShape(
                                                        topStart =
                                                                RentalWheelsDesignSystem
                                                                        .CornerRadius.extraLarge,
                                                        topEnd =
                                                                RentalWheelsDesignSystem
                                                                        .CornerRadius.extraLarge
                                                )
                                        )
                ) {
                    // Car Image
                    AsyncImage(
                            model =
                                    ImageRequest.Builder(LocalContext.current)
                                            .data(
                                                    car.imageUrl.ifEmpty {
                                                        "https://via.placeholder.com/400x300?text=Car"
                                                    }
                                            )
                                            .crossfade(true)
                                            .build(),
                            contentDescription = "${car.make} ${car.model}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                    )

                    // Gradient Overlay
                    Box(
                            modifier =
                                    Modifier.fillMaxSize()
                                            .background(
                                                    Brush.verticalGradient(
                                                            colors =
                                                                    listOf(
                                                                            Color.Transparent,
                                                                            Color.Black.copy(
                                                                                    alpha = 0.3f
                                                                            )
                                                                    ),
                                                            startY = 0f,
                                                            endY = Float.POSITIVE_INFINITY
                                                    )
                                            )
                    )

                    // Favorite Button
                    if (onFavorite != null) {
                        IconButton(
                                onClick = onFavorite,
                                modifier =
                                        Modifier.align(Alignment.TopEnd)
                                                .padding(
                                                        RentalWheelsDesignSystem.Spacing.extraSmall
                                                )
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                    visible = true,
                                    enter =
                                            scaleIn(
                                                    tween(RentalWheelsDesignSystem.Animation.Fast)
                                            ) + fadeIn(),
                                    exit =
                                            scaleOut(
                                                    tween(RentalWheelsDesignSystem.Animation.Fast)
                                            ) + fadeOut()
                            ) {
                                Icon(
                                        imageVector =
                                                if (isFavorite) Icons.Filled.Favorite
                                                else Icons.Outlined.FavoriteBorder,
                                        contentDescription = "Toggle Favorite",
                                        tint =
                                                if (isFavorite)
                                                        RentalWheelsDesignSystem.Colors.Error
                                                else RentalWheelsDesignSystem.Colors.NeutralWhite,
                                        modifier =
                                                Modifier.size(
                                                        RentalWheelsDesignSystem.Spacing.extraLarge
                                                )
                                )
                            }
                        }
                    }

                    // Rating Badge
                    if (car.rating > 0) {
                        Card(
                                modifier =
                                        Modifier.align(Alignment.BottomStart)
                                                .padding(RentalWheelsDesignSystem.Spacing.small),
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        RentalWheelsDesignSystem.Colors.NeutralWhite
                                                                .copy(alpha = 0.95f)
                                        ),
                                shape =
                                        RoundedCornerShape(
                                                RentalWheelsDesignSystem.CornerRadius.medium
                                        )
                        ) {
                            Row(
                                    modifier =
                                            Modifier.padding(
                                                    horizontal =
                                                            RentalWheelsDesignSystem.Spacing
                                                                    .extraSmall,
                                                    vertical = RentalWheelsDesignSystem.Spacing.XXS
                                            ),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement =
                                            Arrangement.spacedBy(
                                                    RentalWheelsDesignSystem.Spacing.XXS
                                            )
                            ) {
                                Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = RentalWheelsDesignSystem.Colors.BrandWarning,
                                        modifier =
                                                Modifier.size(
                                                        RentalWheelsDesignSystem.Spacing.medium
                                                )
                                )
                                Text(
                                        text = String.format("%.1f", car.rating),
                                        style =
                                                MaterialTheme.typography.labelMedium.copy(
                                                        fontWeight = FontWeight.SemiBold,
                                                        color =
                                                                RentalWheelsDesignSystem.Colors
                                                                        .TextPrimary
                                                )
                                )
                            }
                        }
                    }
                }

                // Content Section
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(RentalWheelsDesignSystem.Spacing.medium),
                        verticalArrangement =
                                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.extraSmall)
                ) {
                    // Car Title
                    Text(
                            text = "${car.make} ${car.model}",
                            style =
                                    MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                    ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                    )

                    // Category & Year
                    Text(
                            text =
                                    if (car.year > 0) "${car.category} • ${car.year}"
                                    else car.category,
                            style =
                                    MaterialTheme.typography.bodySmall.copy(
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                    )

                    // Features Row
                    if (variant != CarCardVariant.Compact) {
                        Row(
                                horizontalArrangement =
                                        Arrangement.spacedBy(
                                                RentalWheelsDesignSystem.Spacing.extraSmall
                                        ),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Fuel Type
                            FeatureChip(
                                    icon = Icons.Outlined.LocalGasStation,
                                    text = car.fuelType.ifEmpty { "Gasoline" }
                            )

                            // Transmission
                            FeatureChip(
                                    icon = Icons.Outlined.Speed,
                                    text = car.transmission.ifEmpty { "Auto" }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.XXS))

                    // Pricing Section
                    if (showPricing) {
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                        text =
                                                CurrencyUtils.formatDailyRate(
                                                        if (car.pricePerDay > 0)
                                                                car.pricePerDay.toInt()
                                                        else car.dailyRate
                                                ),
                                        style =
                                                MaterialTheme.typography.titleMedium.copy(
                                                        fontWeight = FontWeight.Bold,
                                                        color = MaterialTheme.colorScheme.primary
                                                )
                                )
                                if (variant == CarCardVariant.Featured) {
                                    Text(
                                            text = "per day",
                                            style =
                                                    MaterialTheme.typography.bodySmall.copy(
                                                            color =
                                                                    MaterialTheme.colorScheme
                                                                            .onSurfaceVariant
                                                    )
                                    )
                                }
                            }

                            // Action Button (for Featured variant only)
                            if (variant == CarCardVariant.Featured) {
                                Button(
                                        onClick = onClick,
                                        shape =
                                                RoundedCornerShape(
                                                        RentalWheelsDesignSystem.CornerRadius.Button
                                                ),
                                        colors =
                                                ButtonDefaults.buttonColors(
                                                        containerColor =
                                                                MaterialTheme.colorScheme.primary
                                                )
                                ) {
                                    Text(
                                            text = "View Details",
                                            style =
                                                    MaterialTheme.typography.labelMedium.copy(
                                                            fontWeight = FontWeight.Medium
                                                    )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureChip(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        text: String,
        modifier: Modifier = Modifier
) {
    Card(
            modifier = modifier,
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainer
                    ),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.small)
    ) {
        Row(
                modifier =
                        Modifier.padding(
                                horizontal = RentalWheelsDesignSystem.Spacing.extraSmall,
                                vertical = RentalWheelsDesignSystem.Spacing.XXS
                        ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.XXS)
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier =
                            Modifier.size(
                                    RentalWheelsDesignSystem.Spacing.ContentSpacing +
                                            RentalWheelsDesignSystem.Spacing.XXS
                            ),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                    text = text,
                    style =
                            MaterialTheme.typography.labelSmall.copy(
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
            )
        }
    }
}
