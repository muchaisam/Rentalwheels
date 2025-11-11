package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.unit.dp
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun AnimatedBackground(
    modifier: Modifier = Modifier,
    primaryColor: Color = MaterialTheme.colorScheme.primary,
    secondaryColor: Color = MaterialTheme.colorScheme.secondary,
    tertiaryColor: Color = MaterialTheme.colorScheme.tertiary
) {
    val infiniteTransition = rememberInfiniteTransition(label = "BackgroundAnimation")

    // Theme-aware opacity adjustments
    val isDarkTheme = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val baseOpacity = if (isDarkTheme) 0.15f else 0.08f

    val animatedOffset1 by
    infiniteTransition.animateFloat(
        initialValue = -200f,
        targetValue = 200f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Offset1"
    )

    val animatedOffset2 by
    infiniteTransition.animateFloat(
        initialValue = 200f,
        targetValue = -200f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Offset2"
    )

    val animatedOffset3 by
    infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 300f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(15000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "Offset3"
    )

    // Rotating color animation
    val animatedRotation by
    infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec =
        infiniteRepeatable(
            animation = tween(20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ColorRotation"
    )

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .background(
                Brush.radialGradient(
                    colors =
                    if (isDarkTheme) {
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme
                                .surfaceVariant.copy(
                                    alpha = 0.3f
                                ),
                            MaterialTheme.colorScheme.background
                        )
                    } else {
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            primaryColor.copy(alpha = 0.02f),
                            MaterialTheme.colorScheme.background
                        )
                    },
                    radius = 1200f
                )
            )
    ) {
        // Primary floating orb
        Box(
            modifier =
            Modifier
                .size(
                    if (isDarkTheme)
                        RentalWheelsDesignSystem.Spacing.Huge * 3.90625f
                    else RentalWheelsDesignSystem.Spacing.Huge * 3.125f
                )
                .offset(
                    x = animatedOffset1.dp,
                    y = RentalWheelsDesignSystem.Spacing.Huge * 1.5625f
                )
                .blur(
                    if (isDarkTheme)
                        RentalWheelsDesignSystem.Spacing.Huge * 1.25f
                    else RentalWheelsDesignSystem.Spacing.Huge * 0.9375f
                )
                .graphicsLayer { rotationZ = animatedRotation * 0.5f }
                .background(
                    primaryColor.copy(alpha = baseOpacity * 2f),
                    CircleShape
                )
        )

        // Secondary floating orb
        Box(
            modifier =
            Modifier
                .size(
                    if (isDarkTheme)
                        RentalWheelsDesignSystem.Spacing.Huge * 2.8125f
                    else RentalWheelsDesignSystem.Spacing.Huge * 2.34375f
                )
                .offset(
                    x = animatedOffset2.dp,
                    y = RentalWheelsDesignSystem.Spacing.Huge * 5.46875f
                )
                .blur(
                    if (isDarkTheme)
                        RentalWheelsDesignSystem.Spacing.Huge * 0.9375f
                    else RentalWheelsDesignSystem.Spacing.Huge * 0.625f
                )
                .graphicsLayer { rotationZ = -animatedRotation * 0.3f }
                .background(
                    secondaryColor.copy(alpha = baseOpacity * 1.5f),
                    CircleShape
                )
        )

        // Tertiary floating orb
        Box(
            modifier =
            Modifier
                .size(
                    if (isDarkTheme)
                        RentalWheelsDesignSystem.Spacing.Huge * 2.1875f
                    else RentalWheelsDesignSystem.Spacing.Huge * 1.875f
                )
                .offset(
                    x = animatedOffset3.dp,
                    y = RentalWheelsDesignSystem.Spacing.Huge * 8.59375f
                )
                .blur(
                    if (isDarkTheme)
                        RentalWheelsDesignSystem.Spacing.Huge * 1.09375f
                    else RentalWheelsDesignSystem.Spacing.Huge * 0.78125f
                )
                .graphicsLayer { rotationZ = animatedRotation * 0.7f }
                .background(tertiaryColor.copy(alpha = baseOpacity), CircleShape)
        )

        // Additional ambient orbs for enhanced dark theme effect
        if (isDarkTheme) {
            Box(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge * 1.5625f)
                    .offset(
                        x = (-animatedOffset1 * 0.5f).dp,
                        y = RentalWheelsDesignSystem.Spacing.Huge * 3.90625f
                    )
                    .blur(RentalWheelsDesignSystem.Spacing.Huge * 0.625f)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(
                            alpha = baseOpacity * 0.5f
                        ),
                        CircleShape
                    )
            )

            Box(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge * 1.25f)
                    .offset(
                        x = (animatedOffset2 * 0.3f).dp,
                        y = RentalWheelsDesignSystem.Spacing.Huge * 7.03125f
                    )
                    .blur(RentalWheelsDesignSystem.Spacing.Huge * 0.46875f)
                    .background(
                        MaterialTheme.colorScheme.tertiary.copy(
                            alpha = baseOpacity * 0.3f
                        ),
                        CircleShape
                    )
            )
        }
    }
}
