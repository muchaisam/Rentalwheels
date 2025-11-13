package com.msdc.rentalwheels.ui.components.home

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

/** Premium Car Card Placeholder with shimmer effect using design system */
@Composable
fun CarCardPlaceholder(modifier: Modifier = Modifier) {
    val shimmerColor = MaterialTheme.colorScheme.surfaceVariant
    val transition = rememberInfiniteTransition(label = "car_placeholder_shimmer")
    val alpha by
    transition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.8f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(RentalWheelsDesignSystem.Animation.Slower),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shimmer_alpha"
    )

    Card(
        modifier = modifier.height(RentalWheelsDesignSystem.Spacing.Huge * 5), // ~320dp
        shape = MaterialTheme.shapes.medium,
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.small
        )
    ) {
        Column {
            // Image placeholder
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .height(
                        RentalWheelsDesignSystem.Spacing.Huge * 2.8125f
                    ) // ~180dp
                    .background(shimmerColor.copy(alpha = alpha))
            )

            // Content placeholders
            Column(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(RentalWheelsDesignSystem.Spacing.medium)
            ) {
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth(0.7f)
                        .height(RentalWheelsDesignSystem.Spacing.extraLarge)
                        .background(
                            shimmerColor.copy(alpha = alpha),
                            shape = MaterialTheme.shapes.small
                        )
                )
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.extraSmall))
                Box(
                    modifier =
                    Modifier
                        .fillMaxWidth(0.5f)
                        .height(RentalWheelsDesignSystem.Spacing.medium)
                        .background(
                            shimmerColor.copy(alpha = alpha),
                            shape = MaterialTheme.shapes.small
                        )
                )
                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    repeat(3) {
                        Box(
                            modifier =
                            Modifier
                                .width(
                                    RentalWheelsDesignSystem.Spacing.Huge * 1.25f
                                ) // ~80dp
                                .height(RentalWheelsDesignSystem.Spacing.medium)
                                .background(
                                    shimmerColor.copy(alpha = alpha),
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                    }
                }
            }
        }
    }
}
