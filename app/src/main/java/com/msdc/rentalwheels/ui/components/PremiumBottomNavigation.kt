package com.msdc.rentalwheels.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.appnavigation.Screen
import com.msdc.rentalwheels.ui.theme.RentalWheelsDesignSystem
import com.msdc.rentalwheels.ui.theme.ThemeState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/** Professional Bottom Navigation Modern glassmorphism design with smooth animations */
@Composable
fun PremiumBottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
    themeState: ThemeState,
    modifier: Modifier = Modifier
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    val isDarkTheme = themeState.isDarkTheme()

    // Navigation Items
    val navigationItems =
        listOf(
            NavigationItem(
                icon = R.drawable.car_home,
                label = "Home",
                route = Screen.Home.route,
                contentDescription = "Navigate to Home screen"
            ),
            NavigationItem(
                icon = R.drawable.maps,
                label = "Browse",
                route = Screen.Browse.route,
                contentDescription = "Navigate to Browse screen"
            ),
            NavigationItem(
                icon = R.drawable.bookings,
                label = "Bookings",
                route = Screen.Bookings.route,
                contentDescription = "Navigate to Bookings screen"
            ),
            NavigationItem(
                icon = R.drawable.settings,
                label = "Settings",
                route = Screen.Settings.route,
                contentDescription = "Navigate to Settings screen"
            )
        )

    // Glassmorphism Navigation Container
    Box(
        modifier =
        modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(
                horizontal = RentalWheelsDesignSystem.Spacing.medium,
                vertical = RentalWheelsDesignSystem.Spacing.extraSmall
            )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.XXL),
            colors =
            CardDefaults.cardColors(
                containerColor =
                if (isDarkTheme) {
                    RentalWheelsDesignSystem.Colors.SurfaceDark02.copy(
                        alpha = 0.95f
                    )
                } else {
                    RentalWheelsDesignSystem.Colors.Surface02.copy(
                        alpha = 0.95f
                    )
                }
            ),
            elevation =
            CardDefaults.cardElevation(
                defaultElevation = RentalWheelsDesignSystem.Elevation.large
            )
        ) {
            Box(
                modifier =
                Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.horizontalGradient(
                            colors =
                            if (isDarkTheme) {
                                listOf(
                                    RentalWheelsDesignSystem
                                        .Colors
                                        .SurfaceDark02.copy(
                                            alpha = 0.9f
                                        ),
                                    RentalWheelsDesignSystem
                                        .Colors
                                        .SurfaceDark03.copy(
                                            alpha = 0.8f
                                        ),
                                    RentalWheelsDesignSystem
                                        .Colors
                                        .SurfaceDark02.copy(
                                            alpha = 0.9f
                                        )
                                )
                            } else {
                                listOf(
                                    RentalWheelsDesignSystem
                                        .Colors.Surface02
                                        .copy(
                                            alpha =
                                            0.95f
                                        ),
                                    RentalWheelsDesignSystem
                                        .Colors.BrandPrimary
                                        .copy(
                                            alpha =
                                            0.05f
                                        ),
                                    RentalWheelsDesignSystem
                                        .Colors.Surface02
                                        .copy(alpha = 0.95f)
                                )
                            }
                        )
                    )
                    .padding(
                        vertical = RentalWheelsDesignSystem.Spacing.small,
                        horizontal = RentalWheelsDesignSystem.Spacing.extraSmall
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    navigationItems.forEachIndexed { index, item ->
                        val isSelected =
                            currentDestination?.route == item.route ||
                                    (currentDestination?.route == null && index == 0)

                        PremiumNavigationItem(
                            icon = item.icon,
                            label = item.label,
                            selected = isSelected,
                            contentDesc = item.contentDescription,
                            isDarkTheme = isDarkTheme,
                            onClick = {
                                scope.launch {
                                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                    delay(50) // Animation delay
                                    onNavigate(
                                        when (index) {
                                            0 -> Screen.Home
                                            1 -> Screen.Browse
                                            2 -> Screen.Bookings
                                            else -> Screen.Settings
                                        }
                                    )
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumNavigationItem(
    icon: Int,
    label: String,
    selected: Boolean,
    contentDesc: String,
    isDarkTheme: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }

    val scale by
    animateFloatAsState(
        targetValue = if (selected) 1.1f else 1f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 300f),
        label = "nav_item_scale"
    )

    val alpha by
    animateFloatAsState(
        targetValue = if (selected) 1f else 0.6f,
        animationSpec = tween(RentalWheelsDesignSystem.Animation.Medium),
        label = "nav_item_alpha"
    )

    Box(
        modifier =
        modifier
            .clip(RoundedCornerShape(RentalWheelsDesignSystem.CornerRadius.medium))
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                role = Role.Tab,
                onClickLabel = contentDesc
            ) { onClick() }
            .semantics {
                role = Role.Tab
                contentDescription = contentDesc
                this.selected = selected
            }
            .padding(RentalWheelsDesignSystem.Spacing.extraSmall)
            .scale(scale),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(RentalWheelsDesignSystem.Spacing.XXS)
        ) {
            // Icon with Selection Background
            Box(
                modifier =
                Modifier
                    .size(
                        RentalWheelsDesignSystem.Spacing.XXL
                    ) // Icon container size
                    .background(
                        color =
                        if (selected) {
                            RentalWheelsDesignSystem.Colors.BrandPrimary
                                .copy(alpha = 0.2f)
                        } else {
                            Color.Transparent
                        },
                        shape =
                        RoundedCornerShape(
                            RentalWheelsDesignSystem.CornerRadius
                                .small
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    modifier =
                    Modifier
                        .size(
                            RentalWheelsDesignSystem.Spacing.large +
                                    RentalWheelsDesignSystem.Spacing.XXS
                        ) // 28dp (24+4)
                        .scale(if (selected) 1.05f else 1f),
                    tint =
                    if (selected) {
                        RentalWheelsDesignSystem.Colors.BrandPrimary
                    } else {
                        if (isDarkTheme) {
                            RentalWheelsDesignSystem.Colors.TextSecondaryDark
                        } else {
                            RentalWheelsDesignSystem.Colors.TextSecondary
                        }
                    }
                        .copy(alpha = alpha)
                )
            }

            // Label - ALWAYS VISIBLE with color change on selection
            Text(
                text = label,
                style =
                MaterialTheme.typography.labelSmall.copy(
                    fontWeight =
                    if (selected) FontWeight.SemiBold
                    else FontWeight.Normal,
                    color =
                    if (selected) {
                        RentalWheelsDesignSystem.Colors.BrandPrimary
                    } else {
                        if (isDarkTheme) {
                            RentalWheelsDesignSystem.Colors
                                .TextSecondaryDark.copy(alpha = 0.7f)
                        } else {
                            RentalWheelsDesignSystem.Colors.TextSecondary
                                .copy(alpha = 0.7f)
                        }
                    }
                ),
                modifier = Modifier.padding(horizontal = RentalWheelsDesignSystem.Spacing.XXS)
            )
        }
    }
}

/** Navigation Item Data Class */
data class NavigationItem(
    val icon: Int,
    val label: String,
    val route: String,
    val contentDescription: String
)
