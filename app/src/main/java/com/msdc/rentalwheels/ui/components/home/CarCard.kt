package com.msdc.rentalwheels.ui.components.home

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.msdc.rentalwheels.data.model.Car
import com.msdc.rentalwheels.data.model.brand
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.utils.CurrencyUtils

/** Premium Car Card with enhanced animations and interactions */
@Composable
fun CarCard(car: Car, onClick: () -> Unit, modifier: Modifier = Modifier) {
    var isBookmarked by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    // Scale animation on press
    val scale by
            animateFloatAsState(
                    targetValue = if (isPressed) 0.97f else 1f,
                    animationSpec =
                            spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessMedium
                            ),
                    label = "car_card_press_scale"
            )

    // Bookmark animation scale
    val bookmarkScale by
            animateFloatAsState(
                    targetValue = if (isBookmarked) 1.2f else 1f,
                    animationSpec =
                            spring(
                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                    stiffness = Spring.StiffnessHigh
                            ),
                    label = "bookmark_scale"
            )

    Card(
            onClick = onClick,
            modifier =
                    modifier.fillMaxWidth()
                            .height(380.dp) // ~RentalWheelsDesignSystem.Spacing.Huge * 5.9
                            .scale(scale)
                            .animateContentSize(
                                    animationSpec =
                                            spring(
                                                    dampingRatio = Spring.DampingRatioMediumBouncy,
                                                    stiffness = Spring.StiffnessLow
                                            )
                            ),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.extraLarge),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation =
                    CardDefaults.cardElevation(
                            defaultElevation = RentalWheelsDesignSystem.Elevation.medium,
                            pressedElevation = RentalWheelsDesignSystem.Elevation.large,
                            hoveredElevation = RentalWheelsDesignSystem.Elevation.large
                    ),
            interactionSource = interactionSource
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Image Section with Enhanced Gradient Overlay
                Box(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(
                                                220.dp
                                        ) // ~RentalWheelsDesignSystem.Spacing.Huge * 3.4
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
                    // Car Image with shimmer placeholder
                    ShimmerAsyncImage(
                            imageUrl = car.imageUrl,
                            contentDescription = "${car.brand} ${car.model}",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                    )

                    // Enhanced gradient overlay for better text visibility
                    Box(
                            modifier =
                                    Modifier.fillMaxSize()
                                            .background(
                                                    brush =
                                                            Brush.verticalGradient(
                                                                    colors =
                                                                            listOf(
                                                                                    Color.Transparent,
                                                                                    Color.Black
                                                                                            .copy(
                                                                                                    alpha =
                                                                                                            0.15f
                                                                                            ),
                                                                                    Color.Black
                                                                                            .copy(
                                                                                                    alpha =
                                                                                                            0.4f
                                                                                            )
                                                                            )
                                                            )
                                            )
                    )

                    // Premium brand badge with glassmorphism
                    Surface(
                            modifier =
                                    Modifier.align(Alignment.TopStart)
                                            .padding(RentalWheelsDesignSystem.Spacing.medium),
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
                            shape =
                                    RoundedCornerShape(
                                            RentalWheelsDesignSystem.CornerRadius.medium
                                    ),
                            tonalElevation = RentalWheelsDesignSystem.Elevation.small,
                            shadowElevation = RentalWheelsDesignSystem.Elevation.extraSmall
                    ) {
                        Text(
                                text = car.brand,
                                modifier =
                                        Modifier.padding(
                                                horizontal = RentalWheelsDesignSystem.Spacing.small,
                                                vertical =
                                                        RentalWheelsDesignSystem.Spacing.extraSmall
                                        ),
                                style = RentalWheelsDesignSystem.Typography.labelLarge,
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Enhanced Bookmark Button with animation
                    Surface(
                            modifier =
                                    Modifier.align(Alignment.TopEnd)
                                            .padding(RentalWheelsDesignSystem.Spacing.medium)
                                            .size(
                                                    48.dp
                                            ) // ~RentalWheelsDesignSystem.Spacing.Huge * 0.75
                                            .scale(bookmarkScale),
                            onClick = { isBookmarked = !isBookmarked },
                            color = MaterialTheme.colorScheme.surface,
                            shape = CircleShape,
                            tonalElevation = RentalWheelsDesignSystem.Elevation.small,
                            shadowElevation = RentalWheelsDesignSystem.Elevation.medium
                    ) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.animation.AnimatedVisibility(
                                    visible = true,
                                    enter =
                                            scaleIn(
                                                    animationSpec =
                                                            tween(
                                                                    RentalWheelsDesignSystem
                                                                            .Animation.Medium
                                                            )
                                            ),
                                    exit =
                                            scaleOut(
                                                    animationSpec =
                                                            tween(
                                                                    RentalWheelsDesignSystem
                                                                            .Animation.Fast
                                                            )
                                            )
                            ) {
                                Icon(
                                        imageVector =
                                                if (isBookmarked) Icons.Filled.Bookmark
                                                else Icons.Outlined.BookmarkBorder,
                                        contentDescription =
                                                if (isBookmarked) "Remove from bookmarks"
                                                else "Add to bookmarks",
                                        tint =
                                                if (isBookmarked) MaterialTheme.colorScheme.primary
                                                else MaterialTheme.colorScheme.onSurfaceVariant,
                                        modifier =
                                                Modifier.size(
                                                        RentalWheelsDesignSystem.Spacing.large
                                                )
                                )
                            }
                        }
                    }
                }

                // Content Section with improved spacing
                Column(
                        modifier =
                                Modifier.fillMaxWidth()
                                        .padding(RentalWheelsDesignSystem.Spacing.extraLarge)
                ) {
                    // Car Name and Year
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                    text = car.model,
                                    style = RentalWheelsDesignSystem.Typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                    text = car.year.toString(),
                                    style = RentalWheelsDesignSystem.Typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier =
                                            Modifier.padding(
                                                    top = RentalWheelsDesignSystem.Spacing.XXS
                                            )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                    // Features Row with improved design
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement =
                                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
                    ) {
                        FeatureChip(
                                icon = Icons.Outlined.LocalGasStation,
                                text = car.fuelType,
                                modifier = Modifier.weight(1f)
                        )
                        FeatureChip(
                                icon = Icons.Outlined.Speed,
                                text = car.transmission,
                                modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Price Section with enhanced styling
                    Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(
                                    text = "Daily Rate",
                                    style = RentalWheelsDesignSystem.Typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                    text = CurrencyUtils.formatDailyRate(car.dailyRate),
                                    style = RentalWheelsDesignSystem.Typography.headlineSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary,
                                    modifier =
                                            Modifier.padding(
                                                    top = RentalWheelsDesignSystem.Spacing.XXS
                                            )
                            )
                        }

                        // Premium CTA button
                        Surface(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape =
                                        RoundedCornerShape(
                                                RentalWheelsDesignSystem.CornerRadius.medium
                                        ),
                                tonalElevation = RentalWheelsDesignSystem.Elevation.small
                        ) {
                            Text(
                                    text = "Book Now",
                                    modifier =
                                            Modifier.padding(
                                                    horizontal =
                                                            RentalWheelsDesignSystem.Spacing.large,
                                                    vertical =
                                                            RentalWheelsDesignSystem.Spacing.small
                                            ),
                                    style = RentalWheelsDesignSystem.Typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                                    fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

/** Enhanced Feature Chip with premium styling */
@Composable
private fun FeatureChip(icon: ImageVector, text: String, modifier: Modifier = Modifier) {
    Surface(
            modifier = modifier,
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
            tonalElevation = RentalWheelsDesignSystem.Elevation.extraSmall
    ) {
        Row(
                modifier =
                        Modifier.fillMaxWidth()
                                .padding(
                                        horizontal = RentalWheelsDesignSystem.Spacing.small,
                                        vertical = RentalWheelsDesignSystem.Spacing.extraSmall
                                ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.medium),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.extraSmall))
            Text(
                    text = text,
                    style = RentalWheelsDesignSystem.Typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/** Image with shimmer loading effect */
@Composable
private fun ShimmerAsyncImage(
        imageUrl: String,
        contentDescription: String,
        modifier: Modifier = Modifier,
        contentScale: ContentScale = ContentScale.Crop
) {
    AsyncImage(
            model = imageUrl,
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = contentScale
    )
}

@Preview(showBackground = true)
@Composable
private fun CarCardPreview() {
    val previewCar =
            Car(
                    make = "Tesla",
                    model = "Model 3",
                    year = 2023,
                    price = 45000,
                    dailyRate = 150,
                    fuelType = "Electric",
                    transmission = "Auto",
                    imageUrl = ""
            )
    CarCard(car = previewCar, onClick = {})
}
