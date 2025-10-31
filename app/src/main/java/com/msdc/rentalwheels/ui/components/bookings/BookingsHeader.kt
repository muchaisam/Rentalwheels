package com.msdc.rentalwheels.ui.components.bookings

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem

@Composable
fun BookingsHeader(
    totalBookings: Int,
    activeBookings: Int,
    upcomingBookings: Int,
    onAnalyticsClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.large),
        elevation =
        CardDefaults.cardElevation(
            defaultElevation = RentalWheelsDesignSystem.Elevation.medium
        )
    ) {
        Box(
            modifier =
            Modifier
                .fillMaxWidth()
                .background(
                    brush =
                    Brush.horizontalGradient(
                        colors =
                        listOf(
                            MaterialTheme.colorScheme
                                .primaryContainer,
                            MaterialTheme.colorScheme
                                .secondaryContainer
                        )
                    ),
                    shape =
                    RoundedCornerShape(
                        RentalWheelsDesignSystem.CornerRadius.large
                    )
                )
        ) {
            Column(modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.large)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "My Bookings",
                            style = RentalWheelsDesignSystem.Typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Track and manage your rentals",
                            style = RentalWheelsDesignSystem.Typography.bodyMedium,
                            color =
                            MaterialTheme.colorScheme.onPrimaryContainer.copy(
                                alpha = 0.8f
                            )
                        )
                    }

                    Surface(
                        onClick = onAnalyticsClick,
                        shape =
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius.medium
                        ),
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                    ) {
                        Box(
                            modifier =
                            Modifier.padding(RentalWheelsDesignSystem.Spacing.medium),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Analytics,
                                contentDescription = "View Analytics",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier =
                                Modifier.size(
                                    RentalWheelsDesignSystem.Spacing.extraLarge
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.large))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement =
                    Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.medium)
                ) {
                    AnimatedBookingStatCard(
                        icon = Icons.Default.CalendarToday,
                        label = "Total",
                        value = totalBookings,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )

                    AnimatedBookingStatCard(
                        icon = Icons.Default.DirectionsCar,
                        label = "Active",
                        value = activeBookings,
                        color = MaterialTheme.colorScheme.tertiary,
                        modifier = Modifier.weight(1f)
                    )

                    AnimatedBookingStatCard(
                        icon = Icons.Default.Schedule,
                        label = "Upcoming",
                        value = upcomingBookings,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AnimatedBookingStatCard(
    icon: ImageVector,
    label: String,
    value: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    val animatedValue by
    animateFloatAsState(
        targetValue = value.toFloat(),
        animationSpec = tween(durationMillis = 800, easing = FastOutSlowInEasing),
        label = "stat_animation"
    )

    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
    ) {
        Column(
            modifier = Modifier.padding(RentalWheelsDesignSystem.Spacing.medium),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier =
                Modifier
                    .size(RentalWheelsDesignSystem.Spacing.Huge)
                    .clip(
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius.medium
                        )
                    )
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = color,
                    modifier = Modifier.size(RentalWheelsDesignSystem.Spacing.large)
                )
            }

            Spacer(modifier = Modifier.height(RentalWheelsDesignSystem.Spacing.small))

            Text(
                text = animatedValue.toInt().toString(),
                style = RentalWheelsDesignSystem.Typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = label,
                style = RentalWheelsDesignSystem.Typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
