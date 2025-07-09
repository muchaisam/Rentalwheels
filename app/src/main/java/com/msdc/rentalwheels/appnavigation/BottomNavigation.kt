package com.msdc.rentalwheels.appnavigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import com.msdc.rentalwheels.R
import com.msdc.rentalwheels.ui.theme.ThemeMode
import com.msdc.rentalwheels.ui.theme.ThemeState
import com.msdc.rentalwheels.ui.theme.Typography
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavigation(
    currentDestination: NavDestination?,
    onNavigate: (Screen) -> Unit,
    themeState: ThemeState
) {
    val haptic = LocalHapticFeedback.current
    val scope = rememberCoroutineScope()

    // Enhanced theme-aware colors
    val primaryAccent = MaterialTheme.colorScheme.primary
    val selectedColor = primaryAccent
    val unselectedColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    val surfaceColor = MaterialTheme.colorScheme.surface
    val themeMode = themeState.themeMode.collectAsState()
    val isDarkTheme = themeMode.value == ThemeMode.Dark

    var previousIndex by remember { mutableStateOf(0) }
    val currentIndex =
        remember(currentDestination) {
            when (currentDestination?.route) {
                Screen.Home.route -> 0
                Screen.Browse.route -> 1
                Screen.Bookings.route -> 2
                Screen.Settings.route -> 3
                else -> 0
            }
        }

    LaunchedEffect(currentDestination) {
        if (currentDestination?.route == null) {
            onNavigate(Screen.Home)
        }
    }

    LaunchedEffect(currentIndex) { previousIndex = currentIndex }

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

    // Glassmorphism surface with proper theme support
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors =
        CardDefaults.cardColors(
            containerColor =
            if (isDarkTheme) {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
            } else {
                MaterialTheme.colorScheme.surface.copy(alpha = 0.98f)
            }
        ),
        elevation =
        CardDefaults.cardElevation(defaultElevation = if (isDarkTheme) 12.dp else 8.dp)
    ) {
        Box(
            modifier =
            Modifier
                .background(
                    Brush.horizontalGradient(
                        colors =
                        if (isDarkTheme) {
                            listOf(
                                MaterialTheme.colorScheme
                                    .surface.copy(
                                        alpha = 0.9f
                                    ),
                                MaterialTheme.colorScheme
                                    .surfaceVariant.copy(
                                        alpha = 0.8f
                                    ),
                                MaterialTheme.colorScheme
                                    .surface.copy(
                                        alpha = 0.9f
                                    )
                            )
                        } else {
                            listOf(
                                MaterialTheme.colorScheme
                                    .surface.copy(
                                        alpha = 0.95f
                                    ),
                                MaterialTheme.colorScheme
                                    .primary.copy(
                                        alpha = 0.05f
                                    ),
                                MaterialTheme.colorScheme
                                    .surface.copy(
                                        alpha = 0.95f
                                    )
                            )
                        }
                    )
                )
                .padding(vertical = 12.dp, horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                navigationItems.forEachIndexed { index, item ->
                    val selected =
                        currentDestination?.route == item.route ||
                                (currentDestination?.route == null && index == 0)

                    ModernNavigationItem(
                        icon = item.icon,
                        label = item.label,
                        selected = selected,
                        contentDesc = item.contentDescription,
                        selectedColor = selectedColor,
                        unselectedColor = unselectedColor,
                        isDarkTheme = isDarkTheme,
                        onSelected = {
                            scope.launch {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                // Add selection animation delay
                                delay(50)
                                onNavigate(
                                    when (index) {
                                        0 -> Screen.Home
                                        1 -> Screen.Browse
                                        2 -> Screen.Bookings
                                        3 -> Screen.Settings
                                        else -> Screen.Home
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

@Composable
private fun ModernNavigationItem(
    icon: Int,
    label: String,
    selected: Boolean,
    contentDesc: String,
    selectedColor: Color,
    unselectedColor: Color,
    isDarkTheme: Boolean,
    onSelected: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val animationScale = remember { Animatable(1f) }

    // Enhanced animations
    val containerScale by
    animateFloatAsState(
        targetValue = if (selected) 1.0f else 0.9f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ContainerScale"
    )

    val iconScale by
    animateFloatAsState(
        targetValue = if (selected) 1.1f else 1.0f,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        label = "IconScale"
    )

    val containerHeight by
    animateDpAsState(
        targetValue = if (selected) 64.dp else 56.dp,
        animationSpec =
        spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "ContainerHeight"
    )

    val backgroundAlpha by
    animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(300),
        label = "BackgroundAlpha"
    )

    // Press animation
    LaunchedEffect(isPressed) {
        if (isPressed) {
            animationScale.animateTo(0.95f, spring(dampingRatio = 0.3f))
        } else {
            animationScale.animateTo(1f, spring(dampingRatio = 0.3f))
        }
    }

    Box(
        modifier =
        Modifier
            .height(containerHeight)
            .clip(RoundedCornerShape(20.dp))
            .scale(animationScale.value)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onSelected
            )
            .semantics {
                this.contentDescription =
                    if (selected) {
                        "Current tab: $contentDesc"
                    } else {
                        contentDesc
                    }
                this.selected = selected
                this.role = Role.Tab
            },
        contentAlignment = Alignment.Center
    ) {
        // Background container for selected state
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn(tween(300)) + scaleIn(spring(dampingRatio = 0.6f)),
            exit = fadeOut(tween(200)) + scaleOut(spring(dampingRatio = 0.8f))
        ) {
            Box(
                modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        brush =
                        Brush.radialGradient(
                            colors =
                            if (isDarkTheme) {
                                listOf(
                                    selectedColor.copy(
                                        alpha = 0.3f
                                    ),
                                    selectedColor.copy(
                                        alpha = 0.1f
                                    ),
                                    Color.Transparent
                                )
                            } else {
                                listOf(
                                    selectedColor.copy(
                                        alpha = 0.2f
                                    ),
                                    selectedColor.copy(
                                        alpha = 0.1f
                                    ),
                                    Color.Transparent
                                )
                            },
                            radius = 120f
                        ),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .alpha(backgroundAlpha)
            )
        }

        // Selection indicator (top)
        AnimatedVisibility(
            visible = selected,
            enter = fadeIn() + expandHorizontally(expandFrom = Alignment.CenterHorizontally),
            exit = fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.CenterHorizontally),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 4.dp)
        ) {
            Box(
                modifier =
                Modifier
                    .width(24.dp)
                    .height(3.dp)
                    .background(selectedColor, RoundedCornerShape(1.5.dp))
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Icon with enhanced animations
            Box(
                contentAlignment = Alignment.Center,
                modifier =
                Modifier
                    .size(28.dp)
                    .scale(containerScale * iconScale)
                    .graphicsLayer {
                        translationY = if (selected) -2f else 0f
                    }
            ) {
                // Glow effect for selected icon
                if (selected) {
                    Icon(
                        painter = painterResource(id = icon),
                        contentDescription = null,
                        tint = selectedColor.copy(alpha = 0.3f),
                        modifier = Modifier
                            .size(32.dp)
                            .blur(8.dp)
                    )
                }

                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = if (selected) selectedColor else unselectedColor,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Label with smooth transitions
            AnimatedVisibility(
                visible = selected,
                enter = fadeIn(tween(300)) + scaleIn(spring(dampingRatio = 0.8f)),
                exit = fadeOut(tween(200)) + scaleOut(spring(dampingRatio = 0.8f))
            ) {
                Text(
                    text = label,
                    style = Typography.labelSmall,
                    color = selectedColor,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
            }
        }

        // Ripple effect overlay
        if (isPressed) {
            Box(
                modifier =
                Modifier
                    .matchParentSize()
                    .background(
                        selectedColor.copy(alpha = 0.1f),
                        RoundedCornerShape(20.dp)
                    )
            )
        }
    }
}

// Navigation item data class
private data class NavigationItem(
    val icon: Int,
    val label: String,
    val route: String,
    val contentDescription: String
)
