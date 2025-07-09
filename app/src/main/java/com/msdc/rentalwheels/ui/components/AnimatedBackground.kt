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
                            MaterialTheme.colorScheme
                                .surface,
                            MaterialTheme.colorScheme
                                .surfaceVariant
                                .copy(alpha = 0.3f),
                            MaterialTheme.colorScheme
                                .background
                        )
                    } else {
                        listOf(
                            MaterialTheme.colorScheme
                                .surface,
                            primaryColor.copy(
                                alpha = 0.02f
                            ),
                            MaterialTheme.colorScheme
                                .background
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
                .size(if (isDarkTheme) 250.dp else 200.dp)
                .offset(x = animatedOffset1.dp, y = 100.dp)
                .blur(if (isDarkTheme) 80.dp else 60.dp)
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
                .size(if (isDarkTheme) 180.dp else 150.dp)
                .offset(x = animatedOffset2.dp, y = 350.dp)
                .blur(if (isDarkTheme) 60.dp else 40.dp)
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
                .size(if (isDarkTheme) 140.dp else 120.dp)
                .offset(x = animatedOffset3.dp, y = 550.dp)
                .blur(if (isDarkTheme) 70.dp else 50.dp)
                .graphicsLayer { rotationZ = animatedRotation * 0.7f }
                .background(
                    tertiaryColor.copy(alpha = baseOpacity),
                    CircleShape
                )
        )

        // Additional ambient orbs for enhanced dark theme effect
        if (isDarkTheme) {
            Box(
                modifier =
                Modifier
                    .size(100.dp)
                    .offset(
                        x = (-animatedOffset1 * 0.5f).dp,
                        y = 250.dp
                    )
                    .blur(40.dp)
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
                    .size(80.dp)
                    .offset(x = (animatedOffset2 * 0.3f).dp, y = 450.dp)
                    .blur(30.dp)
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
