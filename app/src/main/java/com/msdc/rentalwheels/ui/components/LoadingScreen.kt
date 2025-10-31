package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(RentalWheelsDesignSystem.Spacing.medium),
            verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            // Header shimmer
            item { ShimmerPromotionBanner() }

            // Categories shimmer
            item { ShimmerCategorySection() }

            // Cars list shimmer
            items(4) { ShimmerCarCard() }
        }
    }
}

@Composable
private fun ShimmerPromotionBanner() {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(RentalWheelsDesignSystem.Spacing.Huge * 3.125f),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shimmerEffect()
        )
    }
}

@Composable
private fun ShimmerCategorySection() {
    Column {
        // Title shimmer
        Box(
            modifier =
            Modifier
                .width(RentalWheelsDesignSystem.Spacing.Huge * 2.34375f)
                .height(RentalWheelsDesignSystem.Spacing.extraLarge)
                .clip(
                    RoundedCornerShape(
                        RentalWheelsDesignSystem.CornerRadius.small
                    )
                )
                .shimmerEffect()
        )

        Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

        // Categories row shimmer
        Row(horizontalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)) {
            repeat(3) {
                Card(
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.Huge * 1.5625f),
                    shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
                    colors =
                    CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation =
                    CardDefaults.cardElevation(
                        defaultElevation = RentalWheelsDesignSystem.Elevation.small
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmerEffect()
                    )
                }
            }
        }
    }
}

@Composable
private fun ShimmerCarCard() {
    Card(
        modifier =
        Modifier
            .fillMaxWidth()
            .height(RentalWheelsDesignSystem.Spacing.Huge * 2.1875f),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(RentalWheelsDesignSystem.Spacing.medium)
        ) {
            // Car image shimmer
            Box(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge * 1.5625f)
                    .clip(
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius.small
                        )
                    )
                    .shimmerEffect()
            )

            Spacer(modifier = Modifier.width(RentalWheelsDesignSystem.Spacing.medium))

            // Car details shimmer
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement =
                Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.small)
            ) {
                // Title
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth(0.8f)
                        .height(RentalWheelsDesignSystem.Spacing.large)
                        .clip(
                            RoundedCornerShape(
                                RentalWheelsDesignSystem.CornerRadius
                                    .extraSmall
                            )
                        )
                        .shimmerEffect()
                )

                // Subtitle
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth(0.6f)
                        .height(RentalWheelsDesignSystem.Spacing.medium)
                        .clip(
                            RoundedCornerShape(
                                RentalWheelsDesignSystem.CornerRadius
                                    .extraSmall
                            )
                        )
                        .shimmerEffect()
                )

                Spacer(modifier = Modifier.weight(1f))

                // Price
                Box(
                    modifier =
                    Modifier
                        .width(RentalWheelsDesignSystem.Spacing.Huge * 1.25f)
                        .height(RentalWheelsDesignSystem.Spacing.extraLarge)
                        .clip(
                            RoundedCornerShape(
                                RentalWheelsDesignSystem.CornerRadius.small
                            )
                        )
                        .shimmerEffect()
                )
            }
        }
    }
}

private fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val alpha by
    transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(durationMillis = 1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )
    val shimmerColorStops =
        arrayOf(
            0.0f to MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha),
            0.5f to MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha * 0.5f),
            1.0f to MaterialTheme.colorScheme.surfaceVariant.copy(alpha = alpha)
        )
    background(
        brush =
        Brush.linearGradient(
            colorStops = shimmerColorStops,
            start = Offset(0f, 0f),
            end = Offset(1000f, 1000f)
        )
    )
}
