package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import kotlinx.coroutines.delay

/**
 * Professional Splash Screen Component
 *
 * Features:
 * - Smooth scaling animation for the logo
 * - Gradient background with brand colors
 * - Professional typography using design system
 * - Pulsing effect for enhanced visual appeal
 * - Customizable animation duration
 */
@Composable
fun SplashScreen(
    onSplashComplete: () -> Unit = {},
    animationDuration: Long = 2500L,
    modifier: Modifier = Modifier
) {
    // Animation state for logo scaling
    val logoScale = remember { Animatable(0f) }

    // Infinite pulsing animation for the logo
    val infiniteTransition = rememberInfiniteTransition(label = "SplashPulse")
    val pulseAlpha by
    infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1f,
        animationSpec =
        infiniteRepeatable(
            animation =
            tween(
                durationMillis = 1000,
                easing = LinearOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "LogoPulse"
    )

    // Launch entrance animation
    LaunchedEffect(Unit) {
        // Scale logo from 0 to 1 with smooth easing
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200, easing = LinearOutSlowInEasing)
        )

        // Hold for a moment
        delay(animationDuration - 1200)

        // Trigger completion
        onSplashComplete()
    }

    Box(
        modifier =
        modifier
            .fillMaxSize()
            .background(
                brush =
                Brush.verticalGradient(
                    colors =
                    listOf(
                        RentalWheelsDesignSystem.Colors
                            .BrandPrimary,
                        RentalWheelsDesignSystem.Colors
                            .BrandPrimary.copy(
                                alpha = 0.8f
                            ),
                        MaterialTheme.colorScheme
                            .surface
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo Container with Professional Card Design
            Card(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge * 1.875f)
                    .scale(logoScale.value)
                    .graphicsLayer { alpha = pulseAlpha },
                shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
                colors =
                CardDefaults.cardColors(
                    containerColor =
                    MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                ),
                elevation =
                CardDefaults.cardElevation(
                    defaultElevation = RentalWheelsDesignSystem.Elevation.medium
                )
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Icon(
                        painter =
                        painterResource(
                            id = R.drawable.ic_car
                        ), // You'll need to add this icon
                        contentDescription = "RentalWheels Logo",
                        modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.Huge * 0.94f),
                        tint = RentalWheelsDesignSystem.Colors.BrandPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

            // App Name with Professional Typography
            Text(
                text = "RentalWheels",
                style =
                MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .scale(logoScale.value)
                    .graphicsLayer { alpha = pulseAlpha }
            )

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            // Tagline
            Text(
                text = "Your Journey Begins Here",
                style =
                MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center,
                modifier =
                Modifier
                    .scale(logoScale.value)
                    .graphicsLayer {
                        alpha = pulseAlpha * 0.8f
                    }
            )
        }
    }
}

/** Alternative Minimal Splash Screen For when you need a simpler, more subtle splash experience */
@Composable
fun MinimalSplashScreen(onSplashComplete: () -> Unit = {}, modifier: Modifier = Modifier) {
    val logoScale = remember { Animatable(0.8f) }

    LaunchedEffect(Unit) {
        logoScale.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800, easing = LinearOutSlowInEasing)
        )

        delay(1500)
        onSplashComplete()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Simple logo
            Box(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge * 1.25f)
                    .scale(logoScale.value)
                    .background(
                        color = RentalWheelsDesignSystem.Colors.BrandPrimary,
                        shape =
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius
                                .medium
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "RW",
                    style =
                    MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.medium))

            Text(
                text = "RentalWheels",
                style =
                MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface
                ),
                modifier = Modifier.scale(logoScale.value)
            )
        }
    }
}
